package mcs_monitoring.services;

import mcs_monitoring.dtos.MeasurementDTO;
import mcs_monitoring.dtos.builders.MeasurementBuilder;
import mcs_monitoring.entities.Measurement;
import mcs_monitoring.repositories.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final MeasurementRepository mRepository;

    @Autowired
    public MeasurementService(MeasurementRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void saveMeasurement(Measurement measurement) {
        mRepository.save(measurement);
    }

    public void insert(Measurement measurement){
        mRepository.save(measurement);
    }

    public List<MeasurementDTO> getMeasurementsByDeviceId(UUID deviceId) {
        List<Measurement> measurementList = mRepository.findByDeviceId(deviceId);
        return measurementList.stream()
                .map(MeasurementBuilder::toMeasurementDTO)
                .collect(Collectors.toList());
    }

//    public List<Measurement> getLastNMeasurementsByDeviceId(UUID deviceId, int n) {
//        Pageable pageable = PageRequest.of(0, n, Sort.by("timestamp").descending());
//        return mRepository.findByDeviceId(deviceId, pageable);
//    }

    public boolean deleteMeasurement(UUID personId){
        boolean exists = mRepository.findById(personId).isPresent();
        if(exists){
            mRepository.deleteById(personId);
            return true;
        }
        else{
            return false;
        }
    }


}

