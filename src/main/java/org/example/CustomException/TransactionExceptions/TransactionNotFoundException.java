package org.example.CustomException.TransactionExceptions;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(Long transactionId){
        super("Транзакция с id " + transactionId + " не найдена");
    }
}
