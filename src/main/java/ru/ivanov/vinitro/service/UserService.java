package ru.ivanov.vinitro.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.vinitro.model.Role;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.AppointmentRepository;
import ru.ivanov.vinitro.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AppointmentRepository appointmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    @Transactional
    public void save(User user){
        user.addRoleToUser(new Role("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("created in srvc = " + user);
        userRepository.save(user);
    }

    @Transactional
    public void save(String id, User user){
        User existsUser = findById(id).orElse(null);

        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRoles(existsUser.getUserRoles());
        user.setAllAppointments(existsUser.getAllAppointments());
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(String id){
        appointmentRepository.deleteByPatient(findById(id).orElse(null));
        userRepository.deleteById(id);
    }

    @Transactional
    public void addRoleToUser(User user, Role role){
        user.addRoleToUser(role);
        user.setId(user.getId());
        userRepository.save(user);
    }

}
