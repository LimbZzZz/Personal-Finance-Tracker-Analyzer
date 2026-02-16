package org.example.CustomException;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String email){
        super("Email " + email + " уже существует");
    }
}
