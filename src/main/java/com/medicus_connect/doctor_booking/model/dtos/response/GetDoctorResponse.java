package com.medicus_connect.doctor_booking.model.dtos.response;


import com.medicus_connect.doctor_booking.model.common.EducationalDetails;
import com.medicus_connect.doctor_booking.model.common.ExperienceDetails;
import com.medicus_connect.doctor_booking.model.common.PersonalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDoctorResponse {

    public String mobileNo;
    private String department; //one time value -- cannot be updated
    private String haveRegNo;
    private String regNo;
    public PersonalInfo doctorInfo;
    public List<EducationalDetails> educationalDetails;
    public List<ExperienceDetails> experienceDetails;

    public String userName;
    public String password;
}
