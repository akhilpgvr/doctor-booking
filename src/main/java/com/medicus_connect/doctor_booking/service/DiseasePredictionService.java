package com.medicus_connect.doctor_booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicus_connect.doctor_booking.model.common.DiseasePredictionModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiseasePredictionService {

    @Autowired
    private RestTemplate restTemplate;

    public HashMap<String, String> getSpecializationList(){

        Map<String, String> doctorSpecialization = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\akhil\\Documents\\Research\\Doctor_Booking_System\\datasets\\doctor sugession\\dataset\\Doctor_Versus_Disease.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                doctorSpecialization.put(values[0].trim(), values[1].trim());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new HashMap<>(doctorSpecialization);
    }


    public DiseasePredictionModelResponse getPrediction(List<String> symptoms) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
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

        return objectMapper.readValue(response.getBody(), DiseasePredictionModelResponse.class);
    }

    public String suggestDocList(List<String> symptoms) throws JsonProcessingException {

        HashMap<String, String> doctorSpecialization = getSpecializationList();
        DiseasePredictionModelResponse disease = getPrediction(symptoms);
        return doctorSpecialization.getOrDefault(disease.getPrognosis(), "");
    }
}
