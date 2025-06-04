package ru.ivanov.vinitro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface AppointmentRepository extends MongoRepository<AppointmentForAnalysis, String> {
    AppointmentForAnalysis findByDateAndTime(LocalDate date, LocalTime time);
    AppointmentForAnalysis findByTag(int tag);
}
