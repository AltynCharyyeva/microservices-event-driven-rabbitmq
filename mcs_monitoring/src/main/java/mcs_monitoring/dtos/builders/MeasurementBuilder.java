package mcs_monitoring.dtos.builders;

import mcs_monitoring.dtos.MeasurementDTO;
import mcs_monitoring.entities.Measurement;


public class MeasurementBuilder {

    private MeasurementBuilder() {
    }

    public static MeasurementDTO toMeasurementDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getId(), measurement.getUsername(), measurement.getTimestamp(), measurement.getMeasurementValue());
    }


    public static Measurement toEntity(MeasurementDTO measurementDTO) {
        return new Measurement(
                measurementDTO.getId(),
                measurementDTO.getUsername(),
                measurementDTO.getTimestamp(),
                measurementDTO.getMeasurementValue());
    }
}
