package org.example.Validator.UserValidator;

import lombok.AllArgsConstructor;
import org.example.DTO.User.UserDtoRequest;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserValidator {
    private UserNameValidator nameValidator;
    private UserEmailValidator emailValidator;
    private UserPasswordValidator passwordValidator;

    public void fullValidation(UserDtoRequest request){
        nameValidator.validate(request.getName());
        emailValidator.validate(request.getEmail());
        passwordValidator.validate(request.getPassword());
    }

    public void validateName(String name){
        nameValidator.validate(name);
    }

    public void validateEmail(String email){
        emailValidator.validate(email);
    }

    public void validatePassword(String password){
        passwordValidator.validate(password);
    }

}
