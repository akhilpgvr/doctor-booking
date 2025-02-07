package com.medicus_connect.doctor_booking.controller;

import com.medicus_connect.doctor_booking.model.common.AddEmergencyCaseRequest;
import com.medicus_connect.doctor_booking.service.EmergencyCaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emergency")
public class EmergencyCasesController {

    @Autowired
    private EmergencyCaseService emergencyCaseService;

    @Operation(summary = "An endpoint to add emergency cases", description = "")
    @PostMapping("/add")
    public ResponseEntity<String> addEmergencyCases(@RequestBody AddEmergencyCaseRequest addEmergencyCaseRequest){

        log.info("Calling Emergency to save new case for: {}, doctor: {}", addEmergencyCaseRequest.getPatientName(), addEmergencyCaseRequest.getDoctorId());
        return new ResponseEntity<>(emergencyCaseService.addEmergencyCases(addEmergencyCaseRequest), HttpStatus.OK);
    }

}
