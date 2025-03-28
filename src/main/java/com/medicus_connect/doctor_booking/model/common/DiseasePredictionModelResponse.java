package com.medicus_connect.doctor_booking.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseasePredictionModelResponse {

    private String prognosis;
    private long estimated_time;
}
