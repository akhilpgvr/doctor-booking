package com.medicus_connect.doctor_booking.service.client;

import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.dtos.response.GetUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Profile-Management-Client", url = "${config.rest.service.profile-url}")
public interface ProfileMgmtClient {

    @GetMapping("/utility/get-by-userid")
    ResponseEntity<GetUserResponse> getUserAccount(@RequestParam String userId);

    @GetMapping("/utility/get-by-doctorid")
    ResponseEntity<GetDoctorResponse> getDoctorAccount(@RequestParam String doctorId);
}
