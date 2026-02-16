package org.example.CustomException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super("Пользователь " + email + " не найден");
    }

    public UserNotFoundException(Long id){
        super("Пользователь " + id + " не найден");
    }
}
