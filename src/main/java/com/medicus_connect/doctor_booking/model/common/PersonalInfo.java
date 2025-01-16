package com.medicus_connect.doctor_booking.model.common;

import com.medicus_connect.doctor_booking.model.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo {

    public String name;
    public Date dob;
    public Integer age;
    public GenderEnum gender;
    public String email;
}
