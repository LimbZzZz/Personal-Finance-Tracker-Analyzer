package org.example.CustomException;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(){
        System.out.println("Такой email уже существует");
    }
}
