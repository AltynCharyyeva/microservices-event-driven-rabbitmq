package mcs_device.controllers;


import mcs_device.dtos.*;
import mcs_device.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin("http://frontend.localhost")
@CrossOrigin("http://localhost:3000")
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

//    @PostMapping()
//    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
//        UUID deviceID = deviceService.insert(deviceDTO);
//        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
//    }

    @PostMapping("/save")
    public ResponseEntity<UUID> insertDevice(@RequestBody OnlyDeviceDTO deviceDTO) {
        UUID deviceID = deviceService.insertDevice(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(@PathVariable("id") UUID deviceId, @RequestBody DeviceDetailsDTO updatedDevice) {
        DeviceDetailsDTO deviceDTO = deviceService.updateDevice(deviceId, updatedDevice);
        if (deviceDTO != null) {
            return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity DeleteDevice(@PathVariable("id") UUID deviceId){
        boolean deleted = deviceService.deleteDevice(deviceId);
        if(deleted){
            return ResponseEntity.ok("Successfully deleted");
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/person_name_update/{personId}")
    public ResponseEntity<Void> updatePersonName(@PathVariable UUID personId, @RequestBody PersonNameUpdateDTO personNameUpdateDTO) {
        // Ensure the ID from the URL and the body match
        if (!personId.equals(personNameUpdateDTO.getId())) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request if IDs don't match
        }
        if(deviceService.updatePersonName(personId, personNameUpdateDTO)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/associate")
    public ResponseEntity<String> associateDeviceToPerson(@RequestBody DevicePersonAssociationDTO associationDTO) {

        try {
            deviceService.associateDeviceToPerson(associationDTO);
            return ResponseEntity.ok("Device successfully associated with Person.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to associate Device with Person.");
        }
    }



    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceID) {
        DeviceDTO dto = deviceService.findDeviceById(deviceID);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/unlinkPerson/{personId}")
    public ResponseEntity<String> unlinkPersonFromDevice(@PathVariable UUID personId) {
        deviceService.unlinkPersonFromDevices(personId);
        return ResponseEntity.ok("Devices updated successfully");
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByPersonId(@PathVariable UUID personId) {
        List<DeviceDTO> devices = deviceService.getDevicesByPersonId(personId);
        return ResponseEntity.ok(devices);
    }

    //TODO: UPDATE, DELETE per resource

}
