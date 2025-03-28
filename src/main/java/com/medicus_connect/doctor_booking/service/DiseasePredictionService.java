package com.medicus_connect.doctor_booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class DiseasePredictionService {

    @Autowired
    private RestTemplate restTemplate;

    public String getPrediction(List<String> symptoms) {
        String baseUrl = "http://127.0.0.1:8000/api/predict-prognosis/";

        // Build URL with query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        for (String symptom : symptoms) {
            builder.queryParam("symptoms", symptom);
        }

        String url = builder.toUriString(); // Final URL with query params
        System.out.println("Calling Django API: " + url);

        // Make GET request
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}
