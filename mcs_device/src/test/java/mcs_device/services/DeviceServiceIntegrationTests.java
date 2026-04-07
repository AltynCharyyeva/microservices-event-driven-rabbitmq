package mcs_device.services;

import mcs_device.dtos.DeviceDTO;
import mcs_device.dtos.OnlyDeviceDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import mcs_device.Ds2020TestConfig;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class DeviceServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    DeviceService deviceService;

    @Test
    public void testGetCorrect() {
        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals("Test Insert Device", 1, deviceDTOList.size());
    }

    @Test
    public void testInsertCorrectWithGetById() {
        OnlyDeviceDTO d = new OnlyDeviceDTO("Device1", "City 1", 22.3);
        UUID insertedID = deviceService.insertDevice(d);

        DeviceDTO insertedDevice = new DeviceDTO(insertedID, d.getDescription(), d.getAddress(), d.getEnergyConsumption());
        DeviceDTO fetchedDevice = deviceService.findDeviceById(insertedID);

        assertEquals("Test Inserted Device", insertedDevice, fetchedDevice);
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        OnlyDeviceDTO d = new OnlyDeviceDTO("Device1", "City 1", 22.5);
        deviceService.insertDevice(d);

        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals("Test Inserted Persons", 2, deviceDTOList.size());
    }
}
