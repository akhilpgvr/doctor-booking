package com.medicus_connect.doctor_booking.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationalDetails {

    public String school;
    public String Degree;
    public String specialization;
    public Date startDate;
    public Date endDate;
}
