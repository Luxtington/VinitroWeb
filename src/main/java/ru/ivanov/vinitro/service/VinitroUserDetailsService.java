package ru.ivanov.vinitro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.UserRepository;

@Service
public class VinitroUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public VinitroUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        System.out.println("?????? in details service impl");
        return new VinitroUserDetails(user);
    }
}
