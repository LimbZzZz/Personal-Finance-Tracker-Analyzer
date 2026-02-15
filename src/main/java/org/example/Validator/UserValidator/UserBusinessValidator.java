package org.example.Validator.UserValidator;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;
import org.example.DTO.Request.UserDtoRequest;
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


}
