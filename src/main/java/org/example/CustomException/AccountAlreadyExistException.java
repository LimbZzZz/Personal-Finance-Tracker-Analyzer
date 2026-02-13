package org.example.CustomException;

public class AccountAlreadyExistException extends RuntimeException{
    public AccountAlreadyExistException(String cardNumber){
        super("Карта " + cardNumber + "уже существует");
    }
}
