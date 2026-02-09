package org.example.Validator.UserValidator;

import jakarta.validation.ValidationException;
import org.example.Validator.Validateble;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordValidator implements Validateble {
    @Override
    public void validate(String line) {
        String password = line;
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Пароль не может быть пустым");
        }

        if (password.length() < 8) {
            throw new ValidationException("Пароль должен быть не менее 6 символов");
        }

        if (password.length() > 100) {
            throw new ValidationException("Пароль слишком длинный");
        }
    }
}
