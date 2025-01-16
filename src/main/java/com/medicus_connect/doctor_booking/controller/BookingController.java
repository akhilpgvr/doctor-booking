package com.medicus_connect.doctor_booking.controller;

import com.medicus_connect.doctor_booking.model.dtos.request.BookAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.GetAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetAppointmentResponse;
import com.medicus_connect.doctor_booking.service.AppointmentService;
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

    @Operation(summary = "", description = "")
    @PostMapping("/book-appoint")
    public ResponseEntity<String> bookAppointment(@RequestBody BookAppointmentRequest bookAppointmentRequest) {
        return new ResponseEntity<>(appointmentService.bookDoctor(bookAppointmentRequest), HttpStatus.OK);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/get-appoint")
    public ResponseEntity<List<GetAppointmentResponse>> getAppointment(@RequestBody GetAppointmentRequest getAppointmentRequest) {
        return new ResponseEntity<>(appointmentService.getBookings(getAppointmentRequest), HttpStatus.OK);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/delete-appoint")
    public ResponseEntity<String> deleteAppointment(String month) {
        return new ResponseEntity<>(appointmentService.deleteAppointment(), HttpStatus.OK);
    }
}
