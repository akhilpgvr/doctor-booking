package com.medicus_connect.doctor_booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.medicus_connect.doctor_booking.model.dtos.request.BookAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.GetAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.MarkAppointmentOverRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetAppointmentResponse;
import com.medicus_connect.doctor_booking.service.AppointmentService;
import com.medicus_connect.doctor_booking.service.DiseasePredictionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/appointment")
public class BookingController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DiseasePredictionService diseasePredictionService;

    @Operation(summary = "", description = "Endpoint to get the suggested specialist")
    @PostMapping("/suggest-specialist")
    public ResponseEntity<String> suggestSpecialist(@RequestBody List<String> symptoms) throws JsonProcessingException {


        return new ResponseEntity<>(diseasePredictionService.suggestDocList(symptoms), HttpStatus.OK);
    }

    //***********************************************************************************************************//***********************************************************************************************************

    @Operation(summary = "", description = "Endpoint to make an appointment")
    @PostMapping("/book-appoint")
    public ResponseEntity<String> bookAppointment(@RequestBody BookAppointmentRequest bookAppointmentRequest) {
        return new ResponseEntity<>(appointmentService.bookDoctor(bookAppointmentRequest), HttpStatus.OK);
    }

    @Operation(summary = "", description = "Endpoint for user and doctor to get list of appointments by month")
    @PostMapping("/get-appoint")
    public ResponseEntity<List<GetAppointmentResponse>> getAppointment(@RequestBody GetAppointmentRequest getAppointmentRequest) {
        return new ResponseEntity<>(appointmentService.getBookings(getAppointmentRequest), HttpStatus.OK);
    }

    @Operation(summary = "", description = "Endpoint to delete an appointment")
    @GetMapping("/delete-appoint")
    public ResponseEntity<String> deleteAppointment(String month) {
        return new ResponseEntity<>(appointmentService.deleteAppointment(), HttpStatus.OK);
    }


    //***********************************************************************************************************

    @Operation(summary = "", description = "Endpoint to mark appointment is over for a patient")
    @PutMapping("/mark-done")
    public ResponseEntity<String> markAppointmentOver(@RequestBody MarkAppointmentOverRequest markAppointmentOverRequest) {
        return new ResponseEntity<>(appointmentService.markAppointmentOver(markAppointmentOverRequest), HttpStatus.OK);
    }
}
