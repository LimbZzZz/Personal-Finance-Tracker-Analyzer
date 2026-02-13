package org.example.CustomException;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String company){
        super("Такой компании не существует: " + company);
    }
}
