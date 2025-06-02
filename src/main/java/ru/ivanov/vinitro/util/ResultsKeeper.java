package ru.ivanov.vinitro.util;

import ru.ivanov.vinitro.model.AnalysisResult;

import java.util.ArrayList;
import java.util.List;

public class ResultsKeeper {
    private List<AnalysisResult> results = new ArrayList<>();

    public ResultsKeeper() {
    }

    public ResultsKeeper(List<AnalysisResult> results) {
        this.results = results;
    }

    public List<AnalysisResult> getResults() {
        return results;
    }

    public void setResults(List<AnalysisResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ResultsKeeper{" +
                "results=" + results +
                '}';
    }
}
