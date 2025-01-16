package com.medicus_connect.doctor_booking.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAppointmentRequest {

    private String userId;
    private String patientName;
    private String doctorId;
    private Date bookingDate;
    private int startTime;
    private int endTime;
}
