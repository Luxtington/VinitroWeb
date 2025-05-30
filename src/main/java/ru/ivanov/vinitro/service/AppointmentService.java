package ru.ivanov.vinitro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.AnalysisRepository;
import ru.ivanov.vinitro.repository.AppointmentRepository;
import ru.ivanov.vinitro.repository.UserRepository;
import ru.ivanov.vinitro.util.AnalysisStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, AnalysisRepository analysisRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.analysisRepository = analysisRepository;
        this.userRepository = userRepository;
    }

    public List<AppointmentForAnalysis> findAll(){
        return appointmentRepository.findAll();
    }

    public Optional<AppointmentForAnalysis> findById(String id){
        return appointmentRepository.findById(id);
    }

    @Transactional
    public void save(AppointmentForAnalysis appointmentForAnalysis){
        appointmentRepository.save(appointmentForAnalysis);
    }

    @Transactional
    public void deleteById(String id){
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public void appointUserToAnalysis(String analysisId, String patientId, LocalDate date, LocalTime time){
        User patient = userRepository.findById(patientId).orElse(null);
        AppointmentForAnalysis appointment = new AppointmentForAnalysis();
        appointment.setAnalysis(analysisRepository.findById(analysisId).orElse(null));
        appointment.setPatient(patient);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setAnalysisStatus(AnalysisStatus.IN_WAITING);
        save(appointment);

        patient.setId(patientId);
        userRepository.save(patient);
    }
}
