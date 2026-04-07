# Step 1: Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the compiled JAR file into the container at /app
COPY out/artifacts/Producer_jar/Producer.jar /app/Producer.jar

# If the producer depends on external files, copy them into the container as well (e.g., CSV and UUID files)
COPY /text/sensor.csv /app/data/sensor.csv
COPY /text/device.txt /app/data/device.txt


CMD ["java", "-jar", "Producer.jar"]