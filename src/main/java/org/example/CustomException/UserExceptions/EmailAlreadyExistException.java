package org.example.CustomException.UserExceptions;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(){
        System.out.println("Такой email уже существует");
    }
}
