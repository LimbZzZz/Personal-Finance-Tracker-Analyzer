package org.example.CustomException;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(BigDecimal sum){
        super("Не хватает средств, доступно только " + sum);
    }
}
