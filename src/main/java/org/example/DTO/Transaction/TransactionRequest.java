package org.example.DTO.Transaction;

import lombok.Data;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;


@Data
public class TransactionRequest {
    private TransactionType type;
    private String category;
    private String description;
    private BigDecimal sum;
    private Long userId;
}
