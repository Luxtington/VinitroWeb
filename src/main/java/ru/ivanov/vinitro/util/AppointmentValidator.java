package ru.ivanov.vinitro.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class AppointmentValidator implements Validator {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(AppointmentForAnalysis.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LocalDate currentDate = LocalDate.now();
        AppointmentForAnalysis appointment = (AppointmentForAnalysis) target;

        AppointmentForAnalysis existsAppointment = appointmentRepository.findByDateAndTime(appointment.getDate(), appointment.getTime());

        if (existsAppointment!= null && existsAppointment.getAnalysis().getId().equals(appointment.getAnalysis().getId())){
            errors.rejectValue("time", "", "Вы не можете записаться на это время, так как другой пациент уже забронировал это время.");
        }

        if (!isValidDate(appointment.getDate(), appointment.getTime())){
            errors.rejectValue("time", "", "Вы не можете записаться на время, которое уже прошло");
        }

        if (ChronoUnit.DAYS.between(currentDate, appointment.getDate()) > 7){
            errors.rejectValue("date", "", "Вы можете записаться максиумум на неделю вперёд");
        } else{
            System.out.println("epta");
        }
    }

    private boolean isValidDate(LocalDate date, LocalTime time){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (date.isAfter(currentDate)) {
            return true;
        } else if (date.isBefore(currentDate)) {
            return false;
        } else {
            return !time.isBefore(currentTime);
        }
    }
}
