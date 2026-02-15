package org.example.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class CategoryDtoResponse {
    private String name;
    private String color;
    private Map<Long, LocalDateTime> transactionMap;
}
