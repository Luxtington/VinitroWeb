package ru.ivanov.vinitro.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "analysis")
public class Analysis{
    @Id
    private String id;
    @Size(min = 3, max = 30, message = "Некорректна длина в названии анализа, минимум - 3 символа")
    @NotEmpty(message = "Название анализа не может быть пустым")
    private String name;
    private int analysisDuration;
    private List<AnalysisField> fields = new ArrayList<>();
    //private List<AnalysisResult> results = new ArrayList<>();

    public Analysis() {
    }

    public Analysis(String name, int analysisDuration) {
        this.name = name;
        this.analysisDuration = analysisDuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAnalysisDuration() {
        return analysisDuration;
    }

    public void setAnalysisDuration(int analysisDuration) {
        this.analysisDuration = analysisDuration;
    }

    public List<AnalysisField> getFields() {
        System.out.println(fields.size());
        return fields;
    }


    @Override
    public String toString() {
        return "Analysis{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fields=" + fields +
                " + '}'";
    }
}
