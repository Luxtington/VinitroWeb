package ru.ivanov.vinitro.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ivanov.vinitro.model.Role;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.RoleRepository;
import ru.ivanov.vinitro.repository.UserRepository;

@Component
public class DataInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init(){

        if (roleRepository.findByName("ROLE_ADMIN") == null){
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
            LOGGER.info("admin role was uploaded");
        }

        if (roleRepository.findByName("ROLE_NURSE") == null){
            Role roleNurse = new Role("ROLE_NURSE");
            roleRepository.save(roleNurse);
            LOGGER.info("nurse role was uploaded");
        }

        if (roleRepository.findByName("ROLE_ASSISTANT") == null){
            Role roleAssistant = new Role("ROLE_ASSISTANT");
            roleRepository.save(roleAssistant);
            LOGGER.info("assistant role was uploaded");
        }

        if (roleRepository.findByName("ROLE_USER") == null){
            Role roleUser = new Role("ROLE_USER");
            roleRepository.save(roleUser);
            LOGGER.info("user role was uploaded");
        }

        if (userRepository.findByUsername("admin1") == null){
            User admin1 = new User("admin1", "admin1", "admin1", 2025, "vinitro_admin1@mail.ru", "admin1", passwordEncoder.encode("admin1"));
            admin1.addRoleToUser(roleRepository.findByName("ROLE_ADMIN"));
            userRepository.save(admin1);
            LOGGER.info("admin1 was inserted to db");
        }
    }
}
