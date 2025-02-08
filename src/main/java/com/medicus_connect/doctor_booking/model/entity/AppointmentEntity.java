package com.medicus_connect.doctor_booking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointments")
public class AppointmentEntity {

    @Id
    @Generated
    private String id;

    private String userId;
    private String userMailId;
    private String patientName;
    private String doctorId;
    private Date bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDateTime createdOn;
    private String createdBy;
    private LocalDateTime updatedOn;
    private String updatedBy;
}
