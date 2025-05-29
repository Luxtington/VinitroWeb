package ru.ivanov.vinitro.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class AnalysisField {
    @Size(min = 2, max = 15, message = "Некорректная длина названия анализа")
    @NotEmpty(message = "Название анализа не может быть пустым")
    public String name;
    public String type; // num, bool, txt ... may be enum ?

    public AnalysisField() {
    }

    public AnalysisField(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public @Size(min = 2, max = 15, message = "Некорректная длина названия анализа") @NotEmpty(message = "Название анализа не может быть пустым") String getName() {
        return name;
    }

    public void setName(@Size(min = 2, max = 15, message = "Некорректная длина названия анализа") @NotEmpty(message = "Название анализа не может быть пустым") String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisField that = (AnalysisField) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "AnalysisField{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
