package ru.ivanov.vinitro.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.vinitro.repository.AppointmentRepository;

@Component
public class TagValidator implements Validator {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public TagValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(TagKeeper.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TagKeeper tagKeeper = (TagKeeper) target;

        if (appointmentRepository.findByTag(tagKeeper.getTag()) != null){
            errors.rejectValue("tag", "", "Данным тегом уже помечен другой анализ, укажите уникальный тег");
        }
    }
}
