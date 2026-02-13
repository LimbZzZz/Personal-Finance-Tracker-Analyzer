package org.example.CustomException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "ответ с ошибкой")
public class ErrorResponse {
    @Schema(description = "Время ошибки")
    private LocalDateTime timestamp;
    @Schema(description = "HTTP-статус")
    private final int status;
    @Schema(description = "Тип ошибки")
    private final String error;
    @Schema(description = "Детальное сообщение")
    private final String message;
    @Schema(description = "URL ошибки")
    private final String path;
}
