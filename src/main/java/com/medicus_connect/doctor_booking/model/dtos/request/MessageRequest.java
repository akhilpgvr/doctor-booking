package com.medicus_connect.doctor_booking.model.dtos.request;

import com.medicus_connect.doctor_booking.model.common.EmailData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private List<EmailData> emailDataList = new ArrayList<>(); // mailId doctor name, patient name, appointment time
    private String contentCode;
}
