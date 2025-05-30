package ru.ivanov.vinitro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;

@Repository
public interface AppointmentRepository extends MongoRepository<AppointmentForAnalysis, String> {
}
