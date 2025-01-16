package com.medicus_connect.doctor_booking.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentResponse {

    private String userId;
    private String patientName;
    private String doctorId;
    private Date bookingDate;
    private int startTime;
    private int endTime;
}
