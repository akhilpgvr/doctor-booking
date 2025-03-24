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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "emergency-cases")
public class EmergencyCaseEntity {

    @Generated
    @Id
    private String id;

    private String patientName;
    private Integer patientAge;
    private Date admitDate;
    private LocalTime admitTime;
    private String doctorId;
    private String doctorName;
    private String department;

    private List<String> primaryObservations;
    private String disease;
    private boolean msgSend;

    private LocalDateTime createdOn; // admit date is creation date
    private String createdBy;
    private LocalDateTime LastUpdatedOn;
    private String LastUpdatedBy;
}
