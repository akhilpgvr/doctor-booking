package com.medicus_connect.doctor_booking.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentResponse {

    private String userId;
    private String userMailId;
    private String patientName;
    private int patientAge;
    private String doctorId;
    private Date bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean hasAppointmentOccurred;
}
