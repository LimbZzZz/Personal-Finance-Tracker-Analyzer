package org.example.CustomException;

public class CompanyAlreadyExistException extends RuntimeException{
    public CompanyAlreadyExistException(Long id){
        super("Компания c ID: " + id + " уже существует");
    }
}
