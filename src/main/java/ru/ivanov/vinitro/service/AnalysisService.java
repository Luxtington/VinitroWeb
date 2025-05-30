package ru.ivanov.vinitro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.vinitro.model.Analysis;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.AnalysisRepository;
import ru.ivanov.vinitro.repository.UserRepository;
import ru.ivanov.vinitro.util.AnalysisStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;

    @Autowired
    public AnalysisService(AnalysisRepository analysisRepository, UserRepository userRepository) {
        this.analysisRepository = analysisRepository;
        this.userRepository = userRepository;
    }

    public List<Analysis> findAllAnalyses(){
        return analysisRepository.findAll();
    }

    public Optional<Analysis> findById(String id){
        return analysisRepository.findById(id);
    }

    @Transactional
    public void save(Analysis analysis){
        analysisRepository.save(analysis);
    }

    @Transactional
    public void save(String id, Analysis analysis){
        analysis.setId(id);
        analysisRepository.save(analysis);
    }

    @Transactional
    public void deleteById(String id){
        analysisRepository.deleteById(id);
    }

    public boolean checkUserAppointmentForAnalysis(Analysis analysis, String userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user != null){
            return user.isAppointedForAnalysis(analysis.getId());
        }
        else{
            throw new RuntimeException("User not found");
        }
    }
}
