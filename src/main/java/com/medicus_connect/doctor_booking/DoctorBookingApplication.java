package com.medicus_connect.doctor_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAsync  // Enables asynchronous execution
@EnableScheduling
public class DoctorBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorBookingApplication.class, args);
	}

}
