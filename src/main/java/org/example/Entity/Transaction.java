package org.example.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sum")
    private BigDecimal sum;
    @Column(name = "type")
    private TransactionType type;
    @Column(name = "category")
    private String category;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    private void onCreateDate(){
        date = LocalDateTime.now();
    }
}
