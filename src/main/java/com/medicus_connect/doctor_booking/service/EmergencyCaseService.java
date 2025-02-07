package com.medicus_connect.doctor_booking.service;

import com.medicus_connect.doctor_booking.model.common.AddEmergencyCaseRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import com.medicus_connect.doctor_booking.model.entity.EmergencyCaseEntity;
import com.medicus_connect.doctor_booking.repo.EmergencyCaseRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EmergencyCaseService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EmergencyCaseRepo emergencyCaseRepo;

    @Autowired
    private AppointmentService appointmentService;


    public String addEmergencyCases(AddEmergencyCaseRequest request) {

        log.info("Calling ProfileClient for doctor: {}", request.getDoctorId());
        GetDoctorResponse getDoctorResponse = appointmentService.getDoctorByDoctorId(request.getDoctorId());
        EmergencyCaseEntity emergencyCaseEntity = new EmergencyCaseEntity();

        BeanUtils.copyProperties(request, emergencyCaseEntity);
        emergencyCaseEntity.setAdmitTime(LocalTime.of(request.getAdmitHour(), request.getAdmitMinute()));
        emergencyCaseEntity.setDoctorName(getDoctorResponse.getDoctorInfo().getName());
        emergencyCaseEntity.setDepartment(getDoctorResponse.getDepartment());
        emergencyCaseEntity.setCreatedOn(LocalDateTime.now());
        emergencyCaseEntity.setCreatedBy(request.getDoctorId());
        emergencyCaseEntity.setLastUpdatedOn(LocalDateTime.now());
        emergencyCaseEntity.setLastUpdatedBy(request.getDoctorId());
        emergencyCaseRepo.save(emergencyCaseEntity);
        return "Case Added";
    }

    public List<EmergencyCaseEntity>  getEmergencyCases(){

        log.info("Fetching the emergency cases that are not send messages yet");
        return emergencyCaseRepo.findByAdmitDateAndMsgSend(new Date(), false);
    }
    public List<AppointmentEntity> getDelayedAppointmentList() {


        return null;
    }

    public void sendDelayMessage(){

        //Todo Akhil --
        //getEmergencyCases
        //getDelayedAppointmentList
        //call mail service via kafka
    }


}
