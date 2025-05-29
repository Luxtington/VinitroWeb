package ru.ivanov.vinitro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/vinitro/auth/**", "/auth/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/analyses/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_NURSE", "ROLE_USER")
                .requestMatchers("/analyses/*/create").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/analyses/*/delete").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/analyses/*/edit").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/*/change-role").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/*/create").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/*/delete").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/*/edit").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_NURSE")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/vinitro/auth/login")
                .loginProcessingUrl("/vinitro/auth/login")
                .defaultSuccessUrl("/vinitro/index", true)
                .failureUrl("/vinitro/auth/login?error=true")
                .and()
                .logout()
                .logoutUrl("/vinitro/auth/logout")
                .logoutSuccessUrl("/vinitro/auth/login")
                .and()
                .csrf().disable();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
