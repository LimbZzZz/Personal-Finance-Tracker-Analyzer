package org.example.CustomException;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
