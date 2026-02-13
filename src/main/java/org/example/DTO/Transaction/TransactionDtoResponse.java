package org.example.DTO.Transaction;

import lombok.Builder;
import lombok.Data;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDtoResponse {
    private Long id;
    private BigDecimal sum;
    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private String userName;
    private Long userId;
    private String categoryName;
    private String companyName;

}
