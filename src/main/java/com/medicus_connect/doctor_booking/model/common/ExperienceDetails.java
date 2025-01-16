package com.medicus_connect.doctor_booking.model.common;

import com.medicus_connect.doctor_booking.model.enums.EmployeeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDetails {

    public String positionTitle;
    public EmployeeTypeEnum employeeType;
    public String organization;
    public String state;
    public String district;
    public String location;
    public String isCurrentlyWorking;
    public Date startDate;
    public Date endDate;



}
