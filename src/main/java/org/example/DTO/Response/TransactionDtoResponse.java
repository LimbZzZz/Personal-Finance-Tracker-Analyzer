package org.example.DTO.Response;

import lombok.Builder;
import lombok.Data;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class TransactionDtoResponse {
    private Long id;
    private BigDecimal sum;
    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private Map<Long, String> userMap;
    private Map<Long, String> categoryMap;
    private Map<Long, String> companyMap;
}
