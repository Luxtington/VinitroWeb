package ru.ivanov.vinitro.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.UserRepository;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (!userRepository.findByEmail(user.getEmail()).isEmpty()){
            errors.rejectValue("email", "", "Пользователь с таким адресом электронной почты уже зарегистрирован.");
        }
    }
}
