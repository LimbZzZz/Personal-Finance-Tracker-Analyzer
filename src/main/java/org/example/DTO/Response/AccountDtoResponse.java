package org.example.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDtoResponse {
    private BigDecimal totalAccount;
    private String bank;
    private String cardNumber;
    private Long userId;
}
