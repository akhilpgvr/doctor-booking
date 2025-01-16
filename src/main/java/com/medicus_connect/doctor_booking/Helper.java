package com.medicus_connect.profile_mgmt;

import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class Helper {

    public static <S, T> T copyProperties(S source, T destination) {
        BeanUtils.copyProperties(source, destination);
        return destination;
    }

    public static String generateDocId(String mobileNo) {
        return "MEDCONN-USER-"+LocalDateTime.now().getYear()+mobileNo;
    }

    public static String generateUserId(String mobileNo) {
        return "MEDCONN-DOC-"+LocalDateTime.now().getYear()+mobileNo;
    }
}
