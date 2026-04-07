package mcs_device.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import mcs_device.dtos.*;
import mcs_device.dtos.builders.DeviceBuilder;
import mcs_device.entities.Device;
import mcs_device.entities.Person;
import mcs_device.exceptions.DeviceNotFoundException;
import mcs_device.exceptions.PersonNotFoundException;
import mcs_device.repositories.DeviceRepository;
import mcs_device.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mcs_device.controllers.handlers.exceptions.model.ResourceNotFoundException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "device-events";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonRepository personRepository, RabbitTemplate rabbitTemplate) {

        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public DeviceDetailsDTO updateDevice(UUID deviceId, DeviceDetailsDTO  deviceDTO){
        boolean exists = deviceRepository.findById(deviceId).isPresent();
        if (exists){
            Device originalDevice = deviceRepository.findById(deviceId).get();
            if(deviceDTO.getDescription() != null){ // if we update device description
                originalDevice.setDescription(deviceDTO.getDescription());
            }
            if(deviceDTO.getEnergyConsumption() != null){ // if we update device energy consumption
                originalDevice.setEnergyConsumption(deviceDTO.getEnergyConsumption());
                DeviceMessage deviceMessage = new DeviceMessage();
                deviceMessage.setDeviceId(originalDevice.getId());
                deviceMessage.setEnergyConsumption(originalDevice.getEnergyConsumption());

                sendDeviceEventMessage("UPDATE", deviceMessage);
            }
            if(deviceDTO.getAddress() != null){
                originalDevice.setAddress(deviceDTO.getAddress()); // if we update device address
            }
            deviceRepository.save(originalDevice);

            // Create the event and send it to RabbitMQ
//            DeviceEvent deviceEvent;
//            if(originalDevice.getPersonId() == null){
//                deviceEvent = new DeviceEvent(originalDevice.getId(), originalDevice.getEnergyConsumption(), "UPDATE");
//            }
//            else{
//                deviceEvent = new DeviceEvent(originalDevice.getId(), originalDevice.getPersonId(), originalDevice.getEnergyConsumption(), "UPDATE");
//            }
//            System.out.println("DeviceEvent: " + deviceEvent);
//            // Convert the DeviceEvent to a JSON String using ObjectMapper
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                String jsonString = objectMapper.writeValueAsString(deviceEvent);
//
//                // Send the JSON string to RabbitMQ
//                rabbitTemplate.convertAndSend(QUEUE_NAME, jsonString);
//                System.out.println("Device sent(from update): " + jsonString);
//            } catch (IOException e) {
//                LOGGER.error("Error while serializing DeviceEvent to JSON", e);
//            }
            return DeviceBuilder.toDeviceDetailsDTO(originalDevice);
        }
        return null;
    }

    public boolean deleteDevice(UUID deviceId){
        boolean exists = deviceRepository.findById(deviceId).isPresent();
        if(exists){
            deviceRepository.deleteById(deviceId);

            DeviceMessage deviceMessage = new DeviceMessage();
            deviceMessage.setDeviceId(deviceId);
            sendDeviceEventMessage("DELETE", deviceMessage);

            // Create the event and send it to RabbitMQ
//            DeviceEvent deviceEvent = new DeviceEvent(deviceId,  0.00, "DELETE");
//            System.out.println("Device sent(from delete): " + deviceEvent);
//            // Convert the DeviceEvent to a JSON String using ObjectMapper
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                String jsonString = objectMapper.writeValueAsString(deviceEvent);
//
//                // Send the JSON string to RabbitMQ
//                rabbitTemplate.convertAndSend(QUEUE_NAME, jsonString);
//                System.out.println("Device sent(from delete): " + jsonString);
//            } catch (IOException e) {
//                LOGGER.error("Error while serializing DeviceEvent to JSON", e);
//            }
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updatePersonName(UUID personId, PersonNameUpdateDTO personNameUpdateDTO){
        boolean exists = personRepository.findById(personId).isPresent();
        if(exists){
            Person originalPerson = personRepository.findById(personId).get();
            if(personNameUpdateDTO.getName() != null){
                originalPerson.setName(personNameUpdateDTO.getName());
            }
            personRepository.save(originalPerson);
            return true;
        }
        return false;
    }


    public UUID insertDevice(OnlyDeviceDTO deviceDTO) {
        // Convert OnlyDeviceDTO to Device entity
        Device device = DeviceBuilder.toOnlyDeviceEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());

        // Create the event
        DeviceMessage deviceMessage = new DeviceMessage(device.getId(), device.getEnergyConsumption());
        //DeviceEvent deviceEvent = new DeviceEvent(device.getId(), device.getEnergyConsumption());

        // Convert the DeviceEvent to a JSON String using ObjectMapper
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String jsonString = objectMapper.writeValueAsString(deviceEvent);
//
//            // Send the JSON string to RabbitMQ
//            rabbitTemplate.convertAndSend(QUEUE_NAME, jsonString);
//            System.out.println("Device sent(from insert): " + jsonString);
//        } catch (IOException e) {
//            LOGGER.error("Error while serializing DeviceEvent to JSON", e);
//        }
        // send the inserted device to the "device" queue, so that the monitoring
        // microservice can store it to its own database
        sendDeviceEventMessage("INSERT", deviceMessage);

        return device.getId();
    }

    private void sendDeviceEventMessage(String operation, DeviceMessage deviceDetails) {
        DeviceEvent message = new DeviceEvent(operation, deviceDetails);
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }

//    private void sendMessageToDeviceQueue(UUID deviceId, double energyConsumption) {
//        // Create the message as a simple object or a Map
//        DeviceMessage deviceMessage = new DeviceMessage(deviceId, energyConsumption);
//
//        // Send the message to the "device" queue
//        rabbitTemplate.convertAndSend("device", deviceMessage);
//
//        // Log the message event
//        LOGGER.debug("Message sent to 'device' queue with deviceId: {} and energyConsumption: {}", deviceId, energyConsumption);
//    }


    public void associateDeviceToPerson(DevicePersonAssociationDTO associationDTO) {


        Device device = deviceRepository.findById(associationDTO.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        // Fetch the person details from Person Microservice
        String personServiceUrl = "http://reverse-proxy/personservice/person/" + associationDTO.getPersonId();
        System.out.println("PersonServiceURL: "+ personServiceUrl);
        PersonDTO personDetails = restTemplate.getForObject(personServiceUrl, PersonDTO.class);

        if (personDetails == null) {
            throw new PersonNotFoundException("Person not found in Person Microservice");
        }

        // Save or update the person in the Device Microservice database
        Person person = personRepository.findById(personDetails.getId()).orElse(new Person());
        person.setId(personDetails.getId());
        person.setName(personDetails.getName());
        // Set other details if needed

        personRepository.save(person);  // Save person in the Device Microservice database

        // Associate the device with the person
        device.setPersonId(person.getId());
        deviceRepository.save(device);
    }

    public void unlinkPersonFromDevices(UUID personId) {
        // Find devices associated with the personId
        List<Device> devices = deviceRepository.findByPersonId(personId);

        // If devices list is not empty, set person to null and save changes
        if (!devices.isEmpty()) {
            for (Device device : devices) {
                device.setPersonId(null);  // Set the person reference to null for each device
            }
            deviceRepository.saveAll(devices);
        }

        // Delete the person record in the local person table in the device microservice
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
        }
    }

    public List<DeviceDTO> getDevicesByPersonId(UUID personId) {
        List<Device> deviceList = deviceRepository.findByPersonId(personId);
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }


}
