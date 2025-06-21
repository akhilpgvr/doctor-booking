package com.medicus_connect.doctor_booking.enums;

public enum MessageContentTypeEnum {

    DELAY("delay"),
    CANCELLATION("cancellation"),
    SUCCESS("success"),
    ALERT("alert");

    private final String description;

    // Constructor
    MessageContentTypeEnum(String description) {
        this.description = description;
    }

    // Getter method to access the value
    public String getDescription() {
        return description;
    }
}
