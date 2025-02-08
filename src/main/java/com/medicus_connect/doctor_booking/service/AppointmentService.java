package com.medicus_connect.doctor_booking.service;

import ch.qos.logback.core.util.StringUtil;
import com.medicus_connect.doctor_booking.exceptions.SlotNotAvailableException;
import com.medicus_connect.doctor_booking.model.dtos.request.BookAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.request.GetAppointmentRequest;
import com.medicus_connect.doctor_booking.model.dtos.response.GetAppointmentResponse;
import com.medicus_connect.doctor_booking.model.dtos.response.GetDoctorResponse;
import com.medicus_connect.doctor_booking.model.dtos.response.GetUserResponse;
import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import com.medicus_connect.doctor_booking.repo.AppointmentRepo;
import com.medicus_connect.doctor_booking.service.client.ProfileMgmtClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private ProfileMgmtClient profileClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;

    public GetUserResponse getUserByUserId(String userId){
        log.info("Calling ProfileClient for user: {}", userId);
        return profileClient.getUserAccount(userId).getBody();
    }

    public GetDoctorResponse getDoctorByDoctorId(String doctorId){
        log.info("Calling ProfileClient for doctor: {}", doctorId);
        return profileClient.getDoctorAccount(doctorId).getBody();
    }
    public String bookDoctor(BookAppointmentRequest request) {

        log.info("User verification using userId: {}", request.getUserId());
        getUserByUserId(request.getUserId());
        log.info("Doctor verification using doctorId: {}", request.getDoctorId());
        getDoctorByDoctorId(request.getDoctorId());


        LocalTime startTime = LocalTime.of(request.getStartTimeHour(), request.getStartTimeMinute());
        LocalTime endTime = LocalTime.of(request.getEndTimeHour(), request.getEndTimeMinute());
        log.info("Checking booking already exists for doctor: {}", request.getDoctorId());
        if(doesBookingExists(request.getDoctorId(), request.getBookingDate(),  startTime, endTime)) {
            log.error("Booking exists fo this time for: {}", request.getUserId());
            throw new SlotNotAvailableException("Slot Not Available");
        }

        AppointmentEntity booking = new AppointmentEntity();
        BeanUtils.copyProperties(request, booking);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setCreatedOn(LocalDateTime.now());
        booking.setCreatedBy(request.getUserId());
        booking.setUpdatedOn(LocalDateTime.now());
        booking.setUpdatedBy(request.getUserId());
        appointmentRepo.save(booking);
        log.info("Appointment created for {}", request.getUserId());
        return "Appointment Booked";
    }


    public List<GetAppointmentResponse> getBookings(GetAppointmentRequest request) {

        log.info("User verification using userId: {}", request.getUserId());
        getUserByUserId(request.getUserId());
        log.info("Doctor verification using doctorId: {}", request.getDoctorId());
        getDoctorByDoctorId(request.getDoctorId());

        // Get the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set to the first day of the given month
        calendar.set(Calendar.YEAR, currentYear); // Correctly set the year
        calendar.set(Calendar.MONTH, request.getMonth()); // Month is zero-based (0 = January)
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        // Set to the last day of the month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        // Build the query
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(request.getUserId()));
        if(!StringUtil.isNullOrEmpty(request.getDoctorId())) query.addCriteria(Criteria.where("doctorId").is(request.getDoctorId()));
        query.addCriteria(Criteria.where("bookingDate").gte(startDate).lte(endDate));

        // Execute the query
        List<AppointmentEntity> docAvailList = mongoTemplate.find(query, AppointmentEntity.class);

        // Map and return the result
        return docAvailList.stream()
                .map(i -> modelMapper.map(i, GetAppointmentResponse.class))
                .toList();
    }

    public String deleteAppointment() {
        return "";
    }

    public boolean doesBookingExists(String doctorId, Date bookingDate, LocalTime startTime, LocalTime endTime){

        Query query = new Query();
        query.addCriteria(Criteria.where("doctorId").is(doctorId));
        query.addCriteria(Criteria.where("bookingDate").is(bookingDate));
        query.addCriteria(new Criteria().orOperator(Criteria.where("startTime").gte(startTime).lte(endTime), Criteria.where("endTime").gte(startTime).lte(endTime)));
        List<AppointmentEntity> docAvailList = mongoTemplate.find(query, AppointmentEntity.class);
        return !docAvailList.isEmpty();
    }

    public List<AppointmentEntity> getDelayedAppointmentList(String doctorId) {

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

        // Fetch appointments for the given day and doctors

        Query query = new Query();
        log.info("Adding booking range from {} to {} in bookingDate", startOfDay, endOfDay);
        query.addCriteria(Criteria.where("bookingDate").gte(startOfDay).lt(endOfDay));
        log.info("Adding doctorIds for checking criteria");
        query.addCriteria(Criteria.where("doctorId").is(doctorId));

        return mongoTemplate.find(query, AppointmentEntity.class);
    }
}
