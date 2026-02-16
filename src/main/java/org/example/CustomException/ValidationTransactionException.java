package org.example.CustomException;

import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.FieldError;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ValidationTransactionException extends RuntimeException{
    private final List<FieldError> fieldErrors;

    public ValidationTransactionException(List<FieldError> fieldErrors){
        super("Ошибка создания транзакции");
        this.fieldErrors = fieldErrors;
        log.warn("Ошибки возникшие при создании транзакции: {}",
                Arrays.toString(fieldErrors.toArray()));
    }

    public List<FieldError> getFieldErrors(){
        return fieldErrors;
    }
}
