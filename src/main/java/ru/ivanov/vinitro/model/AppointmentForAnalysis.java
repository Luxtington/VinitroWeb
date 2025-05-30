package ru.ivanov.vinitro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.ivanov.vinitro.util.AnalysisStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "appointment")
public class AppointmentForAnalysis {
    @Id
    private String id;
    @DBRef
    private Analysis analysis;
    @DBRef
    private User patient;
    private AnalysisStatus analysisStatus;
    private LocalDate date;
    private LocalTime time;
    private List<AnalysisResult> results = new ArrayList<>();

    public AppointmentForAnalysis() {
    }

    public AppointmentForAnalysis(Analysis analysis, User patient, AnalysisStatus analysisStatus, LocalDate date, LocalTime time) {
        this.analysis = analysis;
        this.patient = patient;
        this.analysisStatus = analysisStatus;
        this.date = date;
        this.time = time;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
        patient.addAnalysisToAnalysisList(this);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<AnalysisResult> getResults() {
        return results;
    }

    public AnalysisStatus getAnalysisStatus() {
        return analysisStatus;
    }

    public void setAnalysisStatus(AnalysisStatus analysisStatus) {
        this.analysisStatus = analysisStatus;
    }

    public boolean isAnalysisInWaitingState(){
        return analysisStatus.equals(AnalysisStatus.IN_WAITING);
    }

    public boolean isAnalysisInProcessing(){
        return analysisStatus.equals(AnalysisStatus.IN_PROCESSING);
    }

    public boolean isAnalysisCompleted(){
        return analysisStatus.equals(AnalysisStatus.COMPLETED);
    }

    @Override
    public String toString() {
        return "AppointmentForAnalysis{" +
                "id='" + id + '\'' +
                ", analysis=" + analysis +
                ", patient=" + patient +
                ", analysisStatus=" + analysisStatus +
                ", date=" + date +
                ", time=" + time +
                ", results=" + results +
                '}';
    }
}
