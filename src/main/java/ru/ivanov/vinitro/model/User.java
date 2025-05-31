package ru.ivanov.vinitro.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.function.Predicate;

@Document(collection = "user")
public class User {
    @Id
    private String id;

    @Size(min = 2, max = 30, message = "Некорректная длина фамилии")
    @NotEmpty(message = "Фамилия не может быть пустой")
    private String surname;

    @Size(min = 2, max = 15, message = "Некорректная длина имени")
    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    @Size(min = 6, max = 20, message = "Некорректная длина отчества")
    private String patronymic;

    @Range(min = 1900, max = 2020, message = "Некорректная дата рождения, должна быть от 1900 до 2020 года")
    private int birthdayYear;

    @Email(message = "Некорректное написание адреса электронной почты")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым")
    private String username;

    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    private Set<Role> userRoles = new HashSet<>();

    // business
    private List<AppointmentForAnalysis> allAnalyses = new ArrayList<>();
//    private List<Analysis> processingAnalyses = new ArrayList<>();
//    private List<Analysis> readyAnalyses = new ArrayList<>();

    public User() {
    }

    public User(String surname, String name, String patronymic, int birthdayYear, String email, String username, String password) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthdayYear = birthdayYear;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @Size(min = 2, max = 30, message = "Некорректная длина фамилии") @NotEmpty(message = "Фамилия не может быть пустой") String getSurname() {
        return surname;
    }

    public void setSurname(@Size(min = 2, max = 30, message = "Некорректная длина фамилии") @NotEmpty(message = "Фамилия не может быть пустой") String surname) {
        this.surname = surname;
    }

    public @Size(min = 2, max = 15, message = "Некорректная длина имени") @NotEmpty(message = "Имя не может быть пустым") String getName() {
        return name;
    }

    public void setName(@Size(min = 2, max = 15, message = "Некорректная длина имени") @NotEmpty(message = "Имя не может быть пустым") String name) {
        this.name = name;
    }

    public @Size(min = 6, max = 20, message = "Некорректная длина отчества") String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(@Size(min = 6, max = 20, message = "Некорректная длина отчества") String patronymic) {
        this.patronymic = patronymic;
    }

    @Range(min = 1900, max = 2020, message = "Некорректная дата рождения, должна быть от 1900 до 2020 года")
    public int getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(@Range(min = 1900, max = 2020, message = "Некорректная дата рождения, должна быть от 1900 до 2020 года") int birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public @Email(message = "Некорректное написание адреса электронной почты") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Некорректное написание адреса электронной почты") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Логин не может быть пустым") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Логин не может быть пустым") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "Пароль не может быть пустым") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "Пароль не может быть пустым") String password) {
        this.password = password;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void addRoleToUser(@NotNull Role role){
        if (!(role.getName().equals("ROLE_USER") || role.getName().equals("ROLE_NURSE") || role.getName().equals("ROLE_ASSISTANT") || role.getName().equals("ROLE_ADMIN"))){
            throw new RuntimeException("Incorrect name of role");
        }
        userRoles.add(role);
    }

    public List<AppointmentForAnalysis> getAllAnalyses() {
        return allAnalyses;
    }

    public boolean isAdmin(){
        int res = (int)userRoles.stream().filter(role -> role.getName().equals("ROLE_ADMIN")).count();
        return res == 1;
    }

    public boolean isNurse(){
        int res = (int)userRoles.stream().filter(role -> role.getName().equals("ROLE_NURSE")).count();
        return res == 1;
    }

    public boolean isAssistant(){
        int res = (int)userRoles.stream().filter(role -> role.getName().equals("ROLE_ASSISTANT")).count();
        return res == 1;
    }

    public boolean isUser(){
        int res = (int)userRoles.stream().filter(role -> role.getName().equals("ROLE_USER")).count();
        return res == 1 && userRoles.size() == 1;
    }

    public void addAnalysisToAnalysisList(@NotNull AppointmentForAnalysis appointmentForAnalysis){
        allAnalyses.add(appointmentForAnalysis);
        System.out.println("analysis was added to analyses list");
    }

    // будет 2 условия: такой айди есть и статус норм (сделано - можно делать еще раз)
    public boolean isAppointedForAnalysis(String analysisId){
        Optional<AppointmentForAnalysis> appointment = allAnalyses.stream()
                      .filter(appointmentForAnalysis -> Objects.equals(appointmentForAnalysis.getAnalysis().getId(), analysisId))
                      .findFirst();
        if (!appointment.isPresent()){
            return false;
        }
        return !appointment.orElse(null).isAnalysisCompleted();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdayYear=" + birthdayYear +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
