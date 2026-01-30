package org.example.DTO.Transaction;

import jakarta.persistence.*;
import lombok.Data;
import org.example.Entity.User;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponce {
    private Long id;
    private BigDecimal sum;
    private TransactionType type;
    private String category;
    private LocalDateTime date;
    private String description;
    private String userName;
    private Long userId;
}
