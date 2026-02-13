package org.example.Validator.UserValidator;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;
import org.example.DTO.User.UserDtoRequest;
import org.example.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserBusinessValidator {
    private final UserRepository userRepository;

    public void fullValidate(UserDtoRequest request){
        List<FieldError> errors = new ArrayList<>();
        emailValidate(request, errors);
        usernameValidate(request, errors);
        passwordValidate(request, errors);

        if(!errors.isEmpty()){
            log.error("Валидация пользователя провалена. Email: {}, Имя: {}, Ошибки: {}",
                    request.getEmail(),
                    request.getName(),
                    errors);
            throw new ValidationException(Arrays.toString(errors.toArray()));
        }else {
            log.debug("Пользователь {} успешно прошел валидацию", request.getEmail());
        }
    }
    public void emailValidate(UserDtoRequest request, List<FieldError> errors) {
        String email = request.getEmail();
        if(email == null || email.trim().isEmpty()){
            errors.add(new FieldError("email",
                    "Email не может быть пустым",
                    "Текущее значение email - " + email));
        }
    }

    public void usernameValidate(UserDtoRequest request, List<FieldError> errors) {
        String username = request.getName();
        if(username == null || username.trim().isEmpty()){
            errors.add(new FieldError("username",
                    "Имя пользователя не может быть пустым",
                    "Текущее значение username - " + username));
        }

        String trimmedName = request.getName().trim();
        if(trimmedName.length() < 2){
            errors.add(new FieldError("username",
                    "Имя должно иметь минимум 2 символа",
                    "Текущее имя - " + trimmedName));
        }

        if(trimmedName.length() > 20){
            errors.add(new FieldError("username",
                    "Имя не должно быть больше 20 символов",
                    "Текущее имя " + trimmedName));
        }
    }

    public void passwordValidate(UserDtoRequest request, List<FieldError> errors) {
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            errors.add(new FieldError("password",
                    "Пароль не может быть пустым",
                    "Текущий пароль - " + password));
        }

        if (password.length() < 8) {
            errors.add(new FieldError("password",
                    "Пароль должен содержать не менее 8 символов",
                    "Текущий пароль -  " + password.length()));
        }

        if (password.length() > 100) {
            errors.add(new FieldError("password",
                    "Пароль должен содержать не более 8 символов",
                    "Текущий пароль - " + password.length()));
        }
    }
}
