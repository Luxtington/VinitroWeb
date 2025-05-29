package ru.ivanov.vinitro.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.vinitro.model.Analysis;
import ru.ivanov.vinitro.repository.AnalysisRepository;

@Component
public class AnalysisValidator implements Validator {

    private final AnalysisRepository analysisRepository;

    @Autowired
    public AnalysisValidator(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Analysis.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Analysis analysis = (Analysis) target;

        if (analysis.getFields().isEmpty()){
            errors.rejectValue("fields", "", "У анализа должна быть как минимум одна характеристика");
        }

        if (!analysisRepository.findByName(analysis.getName()).isEmpty()){
            errors.rejectValue("name", "", "Анализ с таким названием уже существует");
        }
    }
}
