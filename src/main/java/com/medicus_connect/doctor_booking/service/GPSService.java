package com.medicus_connect.doctor_booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GPSService {

    public String convertGPGGAtoGoogleMapsUrl(String gpgga) {
        String[] tokens = gpgga.split(",");
        if (!gpgga.startsWith("$GPGGA") || tokens.length < 6) {
            throw new IllegalArgumentException("Invalid GPGGA sentence");
        }

        String latRaw = tokens[2];     // 0959.1234
        String latDir = tokens[3];     // N
        String lonRaw = tokens[4];     // 07621.5678
        String lonDir = tokens[5];     // E

        double latitude = convertToDecimal(latRaw, latDir);
        double longitude = convertToDecimal(lonRaw, lonDir);

        return String.format("https://www.google.com/maps?q=%f,%f", latitude, longitude);
    }

    private double convertToDecimal(String ddmm, String direction) {
        if (ddmm == null || ddmm.isEmpty()) return 0.0;

        int dotIndex = ddmm.indexOf('.');
        int degreeLength = (dotIndex == -1) ? 2 : dotIndex - 2;

        double degrees = Double.parseDouble(ddmm.substring(0, degreeLength));
        double minutes = Double.parseDouble(ddmm.substring(degreeLength));

        double decimal = degrees + (minutes / 60.0);
        if (direction.equalsIgnoreCase("S") || direction.equalsIgnoreCase("W")) {
            decimal *= -1;
        }

        return decimal;
    }
}
