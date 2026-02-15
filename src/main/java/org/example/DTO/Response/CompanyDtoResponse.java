package org.example.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class CompanyDtoResponse {
    private String name;
    private Map<Long, LocalDateTime> transactionMap;
}
