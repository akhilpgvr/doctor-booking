package com.medicus_connect.doctor_booking.service;

import com.medicus_connect.doctor_booking.enums.MessageContentTypeEnum;
import com.medicus_connect.doctor_booking.model.common.EmailData;
import com.medicus_connect.doctor_booking.model.dtos.request.HospitalEmergencyRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.MessageRequest;
import com.medicus_connect.doctor_booking.service.client.MessagingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static com.medicus_connect.doctor_booking.Utils.EMERGENCY_HOSP_DATA_FILE_PATH;

@Slf4j
@Service
public class IOTAlertService {

    @Autowired
    private MessagingClient messagingClient;
    @Autowired
    private GPSService gpsService;

    public String getEmailByHospitalName(String hospitalName) {
        try (BufferedReader br = new BufferedReader(new FileReader(EMERGENCY_HOSP_DATA_FILE_PATH))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 2); // split into 2 parts
                if (values.length == 2) {
                    String name = values[0].trim();
                    String email = values[1].trim();
                    if (name.equalsIgnoreCase(hospitalName.trim())) {
                        return email;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading CSV file";
        }
        return "Email not found for hospital: " + hospitalName;
    }

    public String alertEmergencyCase(String locationMeta, String hospital, String vehicleNo) {

        log.info("Convert the location metadata: {}", locationMeta);
//        String location = gpsService.convertGPGGAtoGoogleMapsUrl(locationMeta);
        String location = locationMeta;

        //Getting Emergency services Team number of each hospital
        String hospEmail = getEmailByHospitalName(hospital);

        MessageRequest mailRequest = new MessageRequest();
        EmailData emailData = new EmailData();
        emailData.setLocation(location);
        emailData.setVehicleNo(vehicleNo);
        emailData.setMailId(hospEmail);
        mailRequest.getEmailDataList().add(emailData);
        mailRequest.setContentCode(MessageContentTypeEnum.ALERT.getDescription());
        messagingClient.sendTextEmail(mailRequest);
        log.info("Email send to {}", hospEmail);
        return "Alerted successfully";
    }

    public String addEmergencyHospital(List<HospitalEmergencyRequest> request) {

        File file = new File(EMERGENCY_HOSP_DATA_FILE_PATH);

        try {
            boolean fileExists = file.exists();

            // Create file and write header if it doesn't exist
            if (!fileExists) {
                try (FileWriter fw = new FileWriter(file, true)) {
                    fw.write("Hospital Name,Email\n");
                }
            }

            // Read existing lines
            Set<String> existingLines = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    existingLines.add(line.trim());
                }
            }

            // Collect new entries
            List<String> newEntries = new ArrayList<>();
            for (HospitalEmergencyRequest i : request) {
                String[] li = i.getName().trim().split(",");
                String newEntry = li[0]+" "+li[1] + "," + i.getEmail().trim();
                if (!existingLines.contains(newEntry)) {
                    newEntries.add(newEntry);
                }
            }

            // Write all new entries at once
            if (!newEntries.isEmpty()) {
                try (FileWriter fw = new FileWriter(file, true)) {
                    for (String entry : newEntries) {
                        fw.write(entry + "\n");
                    }
                }
            }

            return "Hospital added successfully.";

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }


    public List<String> getEmergencyHospitalDetails() {

        File file = new File(EMERGENCY_HOSP_DATA_FILE_PATH);

        try {
            boolean fileExists = file.exists();

            // Create file and write header if it doesn't exist
            if (!fileExists) {
                throw new RuntimeException("Data Not Present");
            }

            // Check if already exists (optional)
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
            return lines;

        } catch (IOException e) {
            return Collections.singletonList("Error: " + e.getMessage());
        }
    }
}
