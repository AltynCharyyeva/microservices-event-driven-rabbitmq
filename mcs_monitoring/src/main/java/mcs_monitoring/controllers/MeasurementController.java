package mcs_monitoring.controllers;

import mcs_monitoring.dtos.MeasurementDTO;
import mcs_monitoring.entities.Measurement;
import mcs_monitoring.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monitoring")
//@CrossOrigin("http://frontend.localhost")
@CrossOrigin("http://localhost:3000")
public class MeasurementController {

    private final MeasurementService measurementService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public MeasurementController(MeasurementService measurementService){
        this.measurementService = measurementService;
    }

    @GetMapping("/{deviceId}/measurements")
    public ResponseEntity<List<MeasurementDTO>> getMeasurementsByDeviceId(@PathVariable UUID deviceId) {
        List<MeasurementDTO> measurements = measurementService.getMeasurementsByDeviceId(deviceId);

        if (measurements.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no measurements found
        }

        return ResponseEntity.ok(measurements); // 200 OK with the list of measurements
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<MeasurementDTO>> getDevicesByPersonId(@PathVariable UUID personId) {
        List<MeasurementDTO> devices = measurementService.getMeasurementsByDeviceId(personId);
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> DeleteMeasurement(@PathVariable("id") UUID mId){
        boolean deleted = measurementService.deleteMeasurement(mId);
        if(deleted){

            return ResponseEntity.ok("Person successfully deleted");
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    // This method will send a message to all clients subscribed to "/topic/notifications"
    public void sendWarningToUser(String username, UUID deviceId, double sum) {
        String queueName = "/topic/" + username + "/queue/notification";
        String message = "Dear "+ username +", Your device with ID: " + deviceId + " exceeded max hour consumption. Sum: " + sum;
        messagingTemplate.convertAndSend(queueName, message);
        System.out.println("Sent notification to " + queueName);
    }
}
