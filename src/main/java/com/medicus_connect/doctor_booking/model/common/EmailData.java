package com.medicus_connect.doctor_booking.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {

    private String mailId;
    private String patientName;
    private String doctorName;
    private Date appointDate;
    private String appointTime;
    private String newAppointTime;

    //emergency
    private String location;
    private String vehicleNo;
}
