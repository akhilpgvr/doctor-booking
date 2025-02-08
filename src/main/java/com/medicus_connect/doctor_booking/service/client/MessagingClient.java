package com.medicus_connect.doctor_booking.service.client;

import com.medicus_connect.doctor_booking.model.dtos.request.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Messaging-svc-Client", url = "${config.rest.service.messaging-url}")
public interface MessagingClient {

    @PostMapping("/email/sendText")
    void sendTextEmail(@RequestBody MessageRequest messageRequest);
}
