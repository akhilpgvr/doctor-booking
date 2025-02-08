package com.medicus_connect.doctor_booking.service;

import com.medicus_connect.doctor_booking.model.common.AddEmergencyCaseRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import com.medicus_connect.doctor_booking.model.entity.EmergencyCaseEntity;
import com.medicus_connect.doctor_booking.repo.AppointmentRepo;
import com.medicus_connect.doctor_booking.repo.EmergencyCaseRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmergencyCaseService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EmergencyCaseRepo emergencyCaseRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

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

    public int getDelayTime(EmergencyCaseEntity emergencyCase){

        int additionalTime = 30;
        return additionalTime;
    }
    public List<EmergencyCaseEntity> getEmergencyCases(){

        log.info("Fetching the emergency cases that are not send messages yet");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Set to start of the day (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();

        // Set to end of the day (23:59:59)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfDay = calendar.getTime();
        return emergencyCaseRepo.findByAdmitDateRangeAndMsgSend(startOfDay, endOfDay,false);
    }


    public void sendDelayMessage(){

        //Todo Akhil --

        //getEmergencyCases
        log.info("Fetching all emergency cases");
        List<EmergencyCaseEntity> emergencyCases = getEmergencyCases();

        CompletableFuture.runAsync(()-> {

            emergencyCases.forEach(i->{

                //get delay time -- assume to be get in minutes
                int minutesDelay = getDelayTime(i);
                //getDelayedAppointmentList
                List<AppointmentEntity> delayedAppointments = appointmentService.getDelayedAppointmentList(i.getDoctorId());

                delayedAppointments.forEach(k->{

                    //call mail service via kafka

                    //save new appointment time; -- then only it can be updated in a proper way
                    k.setStartTime(k.getStartTime().plusMinutes(minutesDelay));
                    k.setEndTime(k.getEndTime().plusMinutes(minutesDelay));
                    appointmentRepo.save(k);
                });
            });
        });
    }
}
