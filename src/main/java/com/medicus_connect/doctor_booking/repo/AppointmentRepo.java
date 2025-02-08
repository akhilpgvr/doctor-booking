package com.medicus_connect.doctor_booking.repo;

import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepo extends MongoRepository<AppointmentEntity, String> {

    Optional<AppointmentEntity> findByUserIdAndDoctorId(String userId, String doctorId);

    @Query("{ 'bookingDate': { $gte: ?0, $lt: ?1 }, 'doctorId': { $in: ?2 }, sort = \"{ 'startTime': 1 } }")
    List<AppointmentEntity> findByBookingDateRangeAndDoctorIdIn(Date startOfDay, Date endOfDay, List<String> doctorIds);
}
