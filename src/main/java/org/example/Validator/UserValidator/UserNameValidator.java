package org.example.Validator.UserValidator;

import org.example.Validator.Validateble;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;

@Component
public class UserNameValidator implements Validateble {

    @Override
    public void validate(String line) {
        String username = line;
            if(username == null || username.trim().isEmpty()){
                throw new ValidationException("Имя не может быть пустым");
            }

            String trimmedName = line.trim();
            if(trimmedName.length() < 2){
                throw new ValidationException("Имя должно быть не менее 2х символов");
            }

            if(trimmedName.length() > 100){
                throw new ValidationException("Имя должно содержать не более 100 символов");
            }
    }
}
