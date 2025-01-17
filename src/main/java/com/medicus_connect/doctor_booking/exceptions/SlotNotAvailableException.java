package com.medicus_connect.doctor_booking.exceptions;

public class SlotNotAvailableException extends RuntimeException{

    public SlotNotAvailableException(String message){
        super(message);
    }
}
