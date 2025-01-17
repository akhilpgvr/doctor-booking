package com.medicus_connect.doctor_booking.exceptions;

public class UserNotExistsException extends RuntimeException{

    public UserNotExistsException(String message) {
        super(message);
    }
}
