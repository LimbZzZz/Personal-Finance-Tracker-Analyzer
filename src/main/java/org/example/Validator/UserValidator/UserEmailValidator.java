package org.example.Validator.UserValidator;

import jakarta.validation.ValidationException;
import org.example.Validator.Validateble;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidator implements Validateble {
    @Override
    public void validate(String line) {
        String email = line;
            if(email == null || email.trim().isEmpty()){
                throw new ValidationException("email не может быть пустым");
            }
    }
}
