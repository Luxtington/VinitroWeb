package ru.ivanov.vinitro.model;

import java.util.HashMap;
import java.util.Map;

public class AnalysisResult {
    private Map<String, Object> values = new HashMap<>();

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "values=" + values +
                '}';
    }
}
