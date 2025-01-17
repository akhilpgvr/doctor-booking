package com.medicus_connect.doctor_booking.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAppointmentRequest {

    private String userId;
    private String patientName;
    private String doctorId;
    private Date bookingDate;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
}
