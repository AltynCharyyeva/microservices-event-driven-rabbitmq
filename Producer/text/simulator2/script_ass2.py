import time
import json
import os
import pika

# Constants
filename = "sensor.csv"  # Replace with your sensor data filename
deviceId_file = "deviceId.txt"  # File containing the device ID
delay = 10  # Delay in seconds
init_timestamp = 1732399200  # Replace with your starting timestamp

# Read deviceId from a file
try:
    with open(deviceId_file, 'r') as f:
        deviceId = f.readline().strip()
        username = f.readline().strip()
except FileNotFoundError:
    print(f"Device ID file '{deviceId_file}' not found!")
    exit(1)

# Connect to RabbitMQ
url = os.environ.get('CLOUDAMQP_URL', 'amqps://qtqnqmuc:ZcCXnwAsHuRyH0D-jBalU5UTS77PY9Wd@dog-01.lmq.cloudamqp.com/qtqnqmuc')
params = pika.URLParameters(url)
connection = pika.BlockingConnection(params)
channel = connection.channel()
channel.queue_declare(queue='measurement', durable=False)

# Initialize timestamp
timestamp = init_timestamp

# Process the file
with open(filename, 'r') as file:
    lines = file.readlines()  # Read all lines at once
    if len(lines) < 100:
        print("File has fewer than 100 rows!")
        exit(1)

    timestamp = int(time.time() * 1000)
    
    # Start from the 100th row
    prevLine = lines[99].strip()  # Line 100 (index 99)
    for i, line in enumerate(lines[100:], 100):  # Start from 101st row (index 100)
        reading = float(line.strip()) - float(prevLine)
        prevLine = line.strip()

        # Add 600 seconds in milliseconds
        timestamp += 600 * 1000

        jsonObj = {
            "timestamp": timestamp,
            "deviceId": deviceId,
            "measurementValue": reading,
            "username": username
        }

        # Convert into JSON and publish
        y = json.dumps(jsonObj)
        print(y)
        channel.basic_publish(exchange='',
                              routing_key='measurement',
                              body=y)
        print(" [x] Sent sensor data message!")
        time.sleep(float(delay))

connection.close()
