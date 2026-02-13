package org.example.CustomException;

public class CategoryAlreadyExist extends RuntimeException{
    public CategoryAlreadyExist(String name){
        super("Категория " + name + "уже существует");
    }
}
