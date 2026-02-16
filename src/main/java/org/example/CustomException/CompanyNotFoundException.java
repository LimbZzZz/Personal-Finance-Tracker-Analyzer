package org.example.CustomException;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String company){
        super("Такой компании не существует: " + company);
    }
    public CompanyNotFoundException(Long id){
        super("Компании с id " + id + " не существует");
    }
}
