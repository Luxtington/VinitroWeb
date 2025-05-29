package ru.ivanov.vinitro.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter
public class AnalysisResult {
    private String patientId;
    private final Map<AnalysisField, Object> values = new HashMap<>();
}
