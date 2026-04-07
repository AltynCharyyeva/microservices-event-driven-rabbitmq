import com.opencsv.CSVReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Producer {

    private final static String QUEUE_NAME = "measurement";
    private final static String CLOUDAMQP_URL = "amqps://qtqnqmuc:ZcCXnwAsHuRyH0D-jBalU5UTS77PY9Wd@dog-01.lmq.cloudamqp.com/qtqnqmuc";


    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(CLOUDAMQP_URL);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // CSV file path
            String csvFile = "text/simulator2/sensor.csv";
            String uuidFile = "text/device.txt";

            // Read the text file
            List<String> lines = Files.readAllLines(Paths.get(uuidFile));

            // Extract deviceId and userId from the file
            if (lines.size() < 2) {
                throw new IllegalArgumentException("The file must contain at least two lines: deviceId and userId.");
            }

            String deviceIdString = lines.get(0).trim();
            String username = lines.get(1).trim();

            UUID deviceId = UUID.fromString(deviceIdString);
            //UUID userId = UUID.fromString(userIdString);

            //String deviceIdString = new String(Files.readAllBytes(Paths.get(uuidFile))).trim();
            //UUID deviceId = UUID.fromString(deviceIdString);

            // Read the CSV file
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                String[] nextLine;
                reader.readNext(); // Skip header if exists

                // Loop through each line in the CSV
                while ((nextLine = reader.readNext()) != null) {
                    // Assume the CSV file has a single column: measurement_value
                    String measurementValue = nextLine[0]; // Get the measurement value

                    // Get the current timestamp in milliseconds (Unix epoch time)
                    long timestamp = Instant.now().toEpochMilli();

                    // Create a JSON object with the required format
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("timestamp", timestamp);
                    jsonMessage.put("deviceId", deviceId.toString());
                    jsonMessage.put("username", username);
                    jsonMessage.put("measurementValue", Double.parseDouble(measurementValue));

                    // Convert JSON object to string and send to RabbitMQ
                    String message = jsonMessage.toString();
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println(" [x] Sent '" + message + "'");

                    // Wait
                    Thread.sleep(6000);
                }
            }
        }
    }

}
