package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.CustomException.TransactionExceptions.TransactionNotFoundException;
import org.example.CustomException.UserExceptions.UserNotFoundException;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.DTO.Transaction.TransactionDtoResponce;
import org.example.Entity.Company;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Enum.CategoryType;
import org.example.Repository.CategoryRepository;
import org.example.Repository.CompanyRepository;
import org.example.Repository.TransactionRepository;
import org.example.Repository.UserRepository;
import org.example.Validator.TransactionValidator.TransactionBusinessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final TransactionBusinessValidator validator;

    public TransactionDtoResponce createTransaction(TransactionDtoRequest request){
        //validator.fullValidate(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Transaction newTransaction = new Transaction();
        newTransaction.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        newTransaction.setType(request.getType());
        newTransaction.setDescription(request.getDescription());
        newTransaction.setSum(request.getSum());
        newTransaction.setUser(user);
        newTransaction.setDate(LocalDateTime.now());
        newTransaction.addCompanyToCompanyList(companyRepository.getReferenceById(request.getCompanyId()));

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        return mapToTransaction(savedTransaction);
    }

    public List<TransactionDtoResponce> getAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(this::mapToTransaction)
                .collect(Collectors.toList());
    }

    public TransactionDtoResponce getTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapToTransaction(transaction);
    }

    public List<TransactionDtoResponce> getTransactionByCategory(CategoryType category){
        List<TransactionDtoResponce> responces = new ArrayList<>();
        for(Transaction transaction : transactionRepository.findAll()){
            if(transaction.getCategory().equals(category)){
                responces.add(mapToTransaction(transaction));
            }
        }
        return responces;
    }

    private TransactionDtoResponce mapToTransaction(Transaction transaction){
        return TransactionDtoResponce.builder()
                .id(transaction.getId())
                .sum(transaction.getSum())
                .type(transaction.getType())
                .categoryId(transaction.getCategory().getId())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .userName(transaction.getUser().getUsername())
                .userId(transaction.getUser().getId())
                .build();
    }
}
