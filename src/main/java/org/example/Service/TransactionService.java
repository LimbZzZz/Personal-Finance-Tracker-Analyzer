package org.example.Service;

import org.example.CustomException.TransactionNotFoundException;
import org.example.CustomException.UserNotFoundException;
import org.example.DTO.Transaction.TransactionRequest;
import org.example.DTO.Transaction.TransactionResponce;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Repository.TransactionRepository;
import org.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public TransactionResponce createTransaction(TransactionRequest request){
        Transaction newTransaction = new Transaction();

        newTransaction.setCategory(request.getCategory());
        newTransaction.setType(request.getType());
        newTransaction.setDescription(request.getDescription());
        newTransaction.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException(request.getUserId())));

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        return mapToTransaction(newTransaction);
    }

    public List<TransactionResponce> getAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(this::mapToTransaction)
                .collect(Collectors.toList());
    }

    public TransactionResponce getTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapToTransaction(transaction);
    }

    public TransactionResponce mapToTransaction(Transaction transaction){
        TransactionResponce transactionResponce = new TransactionResponce();
        transactionResponce.setId(transaction.getId());
        transactionResponce.setSum(transaction.getSum());
        transactionResponce.setType(transaction.getType());
        transactionResponce.setCategory(transaction.getCategory());
        transactionResponce.setDate(transaction.getDate());
        transactionResponce.setDescription(transaction.getDescription());
        transactionResponce.setSum(transaction.getSum());
        transactionResponce.setUserName(transaction.getUser().getName());
        transactionResponce.setUserId(transaction.getUser().getId());

        return transactionResponce;
    }
}
