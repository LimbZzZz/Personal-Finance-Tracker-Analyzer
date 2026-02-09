package org.example.CustomException.TransactionExceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldError {
    private final String fieldName;
    private final String message;
    private final String invalidValue;
}
