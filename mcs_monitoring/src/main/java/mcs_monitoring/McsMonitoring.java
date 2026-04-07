package mcs_monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import mcs_monitoring.controllers.MeasurementController;
import mcs_monitoring.dtos.DeviceEventDTO;
import mcs_monitoring.entities.Device;
import mcs_monitoring.entities.Measurement;
import mcs_monitoring.services.DeviceService;
import mcs_monitoring.services.MeasurementService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@Validated
public class McsMonitoring extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(McsMonitoring.class);
    }

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private MeasurementController measurementController;

    // Map to store device-specific measurement buckets
    private final Map<UUID, List<Measurement>> deviceMeasurements = new ConcurrentHashMap<>();



    @Bean
    public Queue measurementQueue(){
        return new Queue("measurement", false);
    }

    @RabbitListener(queues = "measurement")
    public void listenMeasurement(String message) {
        try {
            // Parse JSON string to MeasurementEntity object
            Measurement measurement = parseJsonToMeasurementEntity(message);


            // Get the device ID and fetch its maxHourConsumption
            UUID deviceId = measurement.getDeviceId();
            String username = measurement.getUsername();
            Device device = deviceService.findDeviceById(deviceId); // Assuming this method exists
            double maxHourConsumption = device.getEnergyConsumption();

            // Get or initialize the measurement bucket for the device
            deviceMeasurements.putIfAbsent(deviceId, new ArrayList<>());
            List<Measurement> measurements = deviceMeasurements.get(deviceId);

            // Add the new measurement to the bucket
            measurements.add(measurement);

            deviceMeasurements.forEach((devId, meass) -> {
                System.out.println("Device ID: " + devId);
                System.out.println("Measurements:");
                for (Measurement meas : meass) {
                    System.out.println("  - " + meas);  // Assuming toString() is overridden in Measurement class
                }
                System.out.println();  // Add a blank line for separation between devices
            });


            // If the bucket reaches 6 measurements, calculate the sum and save it as hourly consumption
            if (measurements.size() == 6) {
                // Calculate the sum of the measurement values
                double hourlyConsumption = measurements.stream()
                        .mapToDouble(Measurement::getMeasurementValue)
                        .sum();

                measurementService.insert(new Measurement(measurement.getDeviceId(), measurement.getUsername(),
                        measurement.getTimestamp(), hourlyConsumption));


                // Compare the sum with maxHourConsumption
                if (hourlyConsumption > maxHourConsumption) {
                    measurementController.sendWarningToUser(username, deviceId, hourlyConsumption);
                    System.out.println("Warning: Device " + deviceId + " exceeded max hour consumption. Sum: " + hourlyConsumption);
                }
                // Clear the bucket for the next hour
                measurements.clear();
            }

            // Print the message for debugging
            System.out.println(" [x] Received '" + message + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Measurement parseJsonToMeasurementEntity(String json) throws IOException {
        return objectMapper.readValue(json, Measurement.class);
    }


    @Bean
    public Queue deviceQueue(){
        return new Queue("device-events", false);
    }

    @RabbitListener(queues = "device-events")
    public void listenDevice(String message) {
        try {
            // Parse JSON string to DeviceDTOWithOperation object
            DeviceEventDTO deviceEventDTO = parseJsonToDevice(message);
            System.out.println("Operation received: " + deviceEventDTO.getOperation());
            switch(deviceEventDTO.getOperation()) {
                // perform operation -> insert, update, delete
                // check the operation type in the json object
                case "INSERT":
                    deviceService.insert(new Device(deviceEventDTO.getDeviceId(), deviceEventDTO.getEnergyConsumption()));
                    break;
                case "UPDATE":
                    deviceService.update(deviceEventDTO.getDeviceId(), new Device(deviceEventDTO.getDeviceId(), deviceEventDTO.getEnergyConsumption()));
                    break;
                case "DELETE":
                    deviceService.delete(deviceEventDTO.getDeviceId());
                    break;
                default:
                    break;
            }

            // print the message for debug
            System.out.println(" [x] Received '" + message + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(" [x] Received '" + message + "'");
    }

    private DeviceEventDTO parseJsonToDevice(String json) throws IOException {
        return objectMapper.readValue(json, DeviceEventDTO.class);
    }


    public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(McsMonitoring.class, args);
    }
}
