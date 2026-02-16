package org.example.CustomException;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long id){
        super("Счет " + id + " не найден");
    }

    public AccountNotFoundException(String message){
        super(message);
    }
}
