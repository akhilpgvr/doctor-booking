package com.medicus_connect.doctor_booking.exceptions;

public class DoctorNotExistsException extends RuntimeException {

    public DoctorNotExistsException(String message) {
        super(message);
    }
}
