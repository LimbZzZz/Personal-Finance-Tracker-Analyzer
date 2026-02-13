package org.example.CustomException;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id){
        super("Такой категории не существует " + id);
    }
}
