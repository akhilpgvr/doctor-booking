package com.medicus_connect.doctor_booking.exceptions;

public class InsufficientDataException extends RuntimeException{

    public InsufficientDataException(String message){
        super(message);
    }
}
