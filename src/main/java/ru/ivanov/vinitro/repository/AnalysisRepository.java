package ru.ivanov.vinitro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.vinitro.model.Analysis;

import java.util.List;

@Repository
public interface AnalysisRepository extends MongoRepository<Analysis, String> {
    List<Analysis> findByName(String name);
}
