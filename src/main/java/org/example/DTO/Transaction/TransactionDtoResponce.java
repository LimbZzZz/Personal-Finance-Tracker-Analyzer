package org.example.DTO.Transaction;

import lombok.Builder;
import lombok.Data;
import org.example.Enum.CategoryType;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDtoResponce {
    private Long id;
    private BigDecimal sum;
    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private String userName;
    private Long userId;
    private Long categoryId;
    private Long companyId;

}
