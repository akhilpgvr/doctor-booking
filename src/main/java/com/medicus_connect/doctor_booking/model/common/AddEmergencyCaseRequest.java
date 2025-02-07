package com.medicus_connect.doctor_booking.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEmergencyCaseRequest {

    private String patientName;
    private Integer patientAge;
    private Date admitDate;
    private int admitHour;
    private int admitMinute;
    private String doctorId;
    private List<String> primaryObservations;
}
