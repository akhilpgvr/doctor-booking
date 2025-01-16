package com.medicus_connect.doctor_booking.controller;

import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.dtos.response.GetUserResponse;
import com.medicus_connect.doctor_booking.service.client.ProfileMgmtClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/utility")
public class UtilityController {

    @Autowired
    private ProfileMgmtClient profileMgmtClient;
    @Operation(summary = "Api for getting user by userId", description = "")
    @GetMapping("/get-by-userid")
    public ResponseEntity<GetUserResponse> getUserAccount(@RequestParam String userId) {

        log.info("Calling UserService for fetching an account for: {}", userId);
        return new ResponseEntity<>(profileMgmtClient.getUserAccount(userId).getBody(), HttpStatus.OK);
    }

    @Operation(summary = "Api for getting doctor by doctorId", description = "")
    @GetMapping("/get-by-doctorid")
    public ResponseEntity<GetDoctorResponse> getDoctorAccount(@RequestParam String doctorId) {

        log.info("Calling DoctorService for fetching an account for: {}", doctorId);
        return new ResponseEntity<>(profileMgmtClient.getDoctorAccount(doctorId).getBody(), HttpStatus.OK);
    }
}
