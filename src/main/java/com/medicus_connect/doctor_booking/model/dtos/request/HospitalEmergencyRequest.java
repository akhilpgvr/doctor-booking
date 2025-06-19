package com.medicus_connect.doctor_booking.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalEmergencyRequest {

    private String name;
    private String email;
}
