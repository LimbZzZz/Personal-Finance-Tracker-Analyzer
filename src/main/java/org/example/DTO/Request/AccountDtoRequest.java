package org.example.DTO.Request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDtoRequest {
    private BigDecimal totalAccount;
    private String bank;
    private String cardNumber;
    private Long userId;
}
