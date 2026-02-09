package org.example.DTO.Transaction;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.example.Enum.TransactionType;

import java.math.BigDecimal;


@Data
@Builder
public class TransactionDtoRequest {
    @NotNull
    private TransactionType type;
    @Size(max = 50, message = "Описание не должно превышать 50 символов")
    private String description;
    @NotNull(message = "Сумма обязательна")
    @Positive(message = "Число должно быть положительным")
    private BigDecimal sum;
    @NotNull(message = "ID пользователя обязательно")
    @Positive(message = "ID пользователя должен быть положительным")
    private Long userId;
    @NotNull(message = "ID категории не может быть пустой")
    @Positive(message = "ID категории должен быть положительным")
    private Long categoryId;
    @Positive(message = "ID кампании должен быть положительным")
    private Long companyId;
}
