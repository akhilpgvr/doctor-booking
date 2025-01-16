package com.medicus_connect.doctor_booking.service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "Profile-Management-Client", url = "${config.rest.service.profile-url}")
public interface ProfileMgmtClient {
}
