package com.medicus_connect.doctor_booking.repo;

import com.medicus_connect.doctor_booking.model.entity.EmergencyCaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmergencyCaseRepo extends MongoRepository<EmergencyCaseEntity, String> {

    @Query("{ 'admitDate': { $gte: ?0, $lt: ?1 }, 'msgSend': ?2 }")
    List<EmergencyCaseEntity> findByAdmitDateRangeAndMsgSend(Date startOfDay, Date endOfDay, boolean msgSend);
}