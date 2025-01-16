package com.medicus_connect.doctor_booking.repo;

import com.medicus_connect.doctor_booking.model.entity.AppointmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppointmentRepo extends MongoRepository<AppointmentEntity, String> {

    Optional<AppointmentEntity> findByUserIdAndDoctorId(String userId, String doctorId);
}
