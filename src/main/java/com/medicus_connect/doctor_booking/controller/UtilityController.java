package com.medicus_connect.doctor_booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicus_connect.doctor_booking.model.common.DiseasePredictionModelResponse;
import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import com.medicus_connect.doctor_booking.model.entity.EmergencyCaseEntity;
import com.medicus_connect.doctor_booking.service.AppointmentService;
import com.medicus_connect.doctor_booking.service.DiseasePredictionService;
import com.medicus_connect.doctor_booking.service.EmergencyCaseService;
import com.medicus_connect.doctor_booking.service.IOTAlertService;
import com.medicus_connect.doctor_booking.service.client.ProfileMgmtClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/utility")
public class UtilityController {

    @Autowired
    private ProfileMgmtClient profileMgmtClient;
    @Autowired
    private EmergencyCaseService emergencyCaseService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DiseasePredictionService diseasePredictionService;
    @Autowired
    private IOTAlertService iotAlertService;

    @PostMapping("/predict")
    public DiseasePredictionModelResponse predictDisease(@RequestBody List<String> symptoms) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return diseasePredictionService.getPrediction(symptoms);
    }

    @PostMapping("/get-delayed-bookings")
    public ResponseEntity<List<AppointmentEntity>> getDelayedAppointmentList(@RequestBody String doctorIds){
        return new ResponseEntity<>(appointmentService.getDelayedAppointmentList(doctorIds), HttpStatus.OK);
    }

    @GetMapping("/get-emergency-cases")
    public ResponseEntity<List<EmergencyCaseEntity>> getEmergencyCases(){
        return new ResponseEntity<>(emergencyCaseService.getEmergencyCases(), HttpStatus.OK);
    }

    @GetMapping("/send-delay-msg")
    public ResponseEntity<String> sendDelayMsg(){
        emergencyCaseService.sendDelayMessage();
        return new ResponseEntity<>("Message Send", HttpStatus.OK);
    }

    @GetMapping("/getEmailByHospitalName")
    public ResponseEntity<String> getEmailByHospitalName(String hospName){
        return new ResponseEntity<>(iotAlertService.getEmailByHospitalName(hospName), HttpStatus.OK);
    }
}
