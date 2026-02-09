package org.example.CustomException.TransactionExceptions;

import java.util.Arrays;
import java.util.List;

public class ValidationTransactionException extends RuntimeException{
    private final List<FieldError> fieldErrors;

    public ValidationTransactionException(List<FieldError> fieldErrors){
        super("Ошибка создания транзакции");
        this.fieldErrors = fieldErrors;
        System.out.println(Arrays.toString(fieldErrors.toArray()));
    }

    public List<FieldError> getFieldErrors(){
        return fieldErrors;
    }
}
