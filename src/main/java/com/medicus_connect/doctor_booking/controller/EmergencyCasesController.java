package com.medicus_connect.doctor_booking.controller;

import com.medicus_connect.doctor_booking.model.common.AddEmergencyCaseRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.HospitalEmergencyRequest;
import com.medicus_connect.doctor_booking.service.EmergencyCaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "An endpoint to alert EmergencyCase", description = "")
    @PostMapping("/alert")
    public ResponseEntity<String> alertEmergencyCase(@RequestParam String location, @RequestParam String vehicleNo){

        log.info("Emergency alert for the vehicle: {}", vehicleNo); // Anyone can travel in the vehicle there is no fixed users. Module is set in vehicle.
        // ToDO Akhil -- Therefore use vehicleNo instead of person to identify the accident. Later on update to the Emergency log
        return new ResponseEntity<>(emergencyCaseService.alertEmergencyCase(location, vehicleNo), HttpStatus.OK);
    }

    @Operation(summary = "An endpoint to add Emergency Case Hospital contact info", description = "")
    @PostMapping("/add/emer-hos-data")
    public ResponseEntity<String> addEmergencyHospital(@RequestBody List<HospitalEmergencyRequest> hospitalEmergencyRequest) {

        log.info("add Emergency Case Hospital contact info");

        return new ResponseEntity<>(emergencyCaseService.addEmergencyHospital(hospitalEmergencyRequest), HttpStatus.OK);
    }

    @Operation(summary = "An endpoint to get Emergency Case Hospital contact info", description = "")
    @GetMapping("/get/emer-hos-data")
    public ResponseEntity<List<String>> getEmergencyHospital() {

        log.info("get Emergency Case Hospital contact info");

        return new ResponseEntity<>(emergencyCaseService.getEmergencyHospital(), HttpStatus.OK);
    }


}
