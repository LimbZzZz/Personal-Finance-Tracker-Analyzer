package org.example.CustomException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FieldError {
    private final String fieldName;
    private final String message;
    private final String invalidValue;
}
