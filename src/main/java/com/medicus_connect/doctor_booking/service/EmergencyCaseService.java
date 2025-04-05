package com.medicus_connect.doctor_booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicus_connect.doctor_booking.enums.MessageContentTypeEnum;
import com.medicus_connect.doctor_booking.exceptions.DiseasePredictionException;
import com.medicus_connect.doctor_booking.model.common.AddEmergencyCaseRequest;
import com.medicus_connect.doctor_booking.model.common.DiseasePredictionModelResponse;
import com.medicus_connect.doctor_booking.model.common.EmailData;
import com.medicus_connect.doctor_booking.model.dtos.request.MessageRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import com.medicus_connect.doctor_booking.model.entity.EmergencyCaseEntity;
import com.medicus_connect.doctor_booking.repo.AppointmentRepo;
import com.medicus_connect.doctor_booking.repo.EmergencyCaseRepo;
import com.medicus_connect.doctor_booking.service.client.MessagingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    private MessagingClient messagingClient;

    @Autowired
    private DiseasePredictionService diseasePredictionService;


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

    public DiseasePredictionModelResponse getDiseaseAndDelayTime(EmergencyCaseEntity emergencyCase) throws JsonProcessingException {

        //RestTemplate connection to client
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[Prediction Model] -- calling ML model for predicting disease and time");
        String res = diseasePredictionService.getPrediction(emergencyCase.getPrimaryObservations());
        return objectMapper.readValue(res, DiseasePredictionModelResponse.class);
    }

    public List<EmergencyCaseEntity> getEmergencyCases(){

        log.info("Fetching the emergency cases that are not send messages yet");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // TODO Akhil -- Set to current day time (00:00:00)
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

    @Scheduled(cron = "0 */3 * * * ?")
    public void sendDelayMessage() {

        //getEmergencyCases
        log.info("Fetching all emergency cases");
        List<EmergencyCaseEntity> emergencyCases = getEmergencyCases();
        AtomicReference<DiseasePredictionModelResponse> modelResponse = new AtomicReference<>(new DiseasePredictionModelResponse());

        emergencyCases.forEach(i -> {
            //get delay time -- assume to be get in minutes

            modelResponse.set(new DiseasePredictionModelResponse());
            log.info("Calling service for predicting disease and time");
            try {
                modelResponse.set(getDiseaseAndDelayTime(i));
            } catch (Exception e) {
                throw new DiseasePredictionException(e.getMessage());
            }

            //getDelayedAppointmentList
            List<AppointmentEntity> delayedAppointments = appointmentService.getDelayedAppointmentList(i.getDoctorId());
            delayedAppointments.forEach(k -> {
                CompletableFuture.runAsync(() -> {
                    //TODO Akhil -- call mail service via kafka
                    MessageRequest request = new MessageRequest();
                    EmailData emailData = new EmailData();
                    emailData.setMailId(k.getUserMailId());
                    emailData.setPatientName(k.getPatientName());
                    emailData.setDoctorName(i.getDoctorName());
                    emailData.setAppointDate(new Date());
                    //TODO AKhil --repeated delay issue is ignored --updated on Appointment entity
                    emailData.setAppointTime(k.getStartTime().toString());
                    emailData.setNewAppointTime(k.getStartTime().plusMinutes(modelResponse.get().getEstimated_time()).toString());
                    request.getEmailDataList().add(emailData);
                    request.setContentCode(MessageContentTypeEnum.DELAY.getDescription());
                    messagingClient.sendTextEmail(request);
                    log.info("Email send to {}", k.getUserMailId());

                    //save new appointment time; -- then only it can be updated in a proper way
                    k.setStartTime(k.getStartTime().plusMinutes(modelResponse.get().getEstimated_time()));
                    k.setEndTime(k.getEndTime().plusMinutes(modelResponse.get().getEstimated_time()));
                    appointmentRepo.save(k);
                    log.info("updated appointment entity with new appointment time");
                    //make msgSend true, updating disease and update the entity
                    i.setMsgSend(true);
                    if(i.getDisease().isBlank()) i.setDisease(modelResponse.get().getPrognosis());
                    emergencyCaseRepo.save(i);
                    log.info("updated msgSend in emergency case entity");
                });
            });
        });
    }
}
