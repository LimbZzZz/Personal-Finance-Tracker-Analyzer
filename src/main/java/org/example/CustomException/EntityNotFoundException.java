package org.example.CustomException;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entity){
        super("Сущность " + entity + " не найдена");
    }
}
