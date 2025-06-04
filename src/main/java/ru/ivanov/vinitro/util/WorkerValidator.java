package ru.ivanov.vinitro.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;

@Component
public class WorkerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(AppointmentForAnalysis.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AppointmentForAnalysis appointment = (AppointmentForAnalysis) target;

        if (appointment.getPatient().isAppointedForAnalysis(appointment.getAnalysis().getId())){
            errors.rejectValue("analysis", "", "Невозможно подтвердить сдачу анализа пациенту, который уже записан на этот анализ");
        }
    }
}
