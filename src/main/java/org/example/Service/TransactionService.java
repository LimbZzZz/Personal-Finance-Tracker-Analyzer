package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.TransactionNotFoundException;
import org.example.CustomException.UserNotFoundException;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.DTO.Transaction.TransactionDtoResponse;
import org.example.Entity.Transaction;
import org.example.Entity.User;
import org.example.Repository.CategoryRepository;
import org.example.Repository.CompanyRepository;
import org.example.Repository.TransactionRepository;
import org.example.Repository.UserRepository;
import org.example.Service.AOP.LogExecutionTime;
import org.example.Validator.TransactionValidator.TransactionBusinessValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService{
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionBusinessValidator validator;

    @LogExecutionTime(description = "Создание транзакции")
    public TransactionDtoResponse createTransaction(TransactionDtoRequest request){
        validator.fullValidate(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Transaction newTransaction = new Transaction();
        newTransaction.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException()));
        newTransaction.setType(request.getType());
        newTransaction.setDescription(request.getDescription());
        newTransaction.setSum(request.getSum());
        newTransaction.setUser(user);
        newTransaction.setDate(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        return mapToTransaction(savedTransaction);
    }

    @LogExecutionTime(description = "Получить все транзакции")
    public List<TransactionDtoResponse> getAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(this::mapToTransaction)
                .collect(Collectors.toList());
    }

    @LogExecutionTime(description = "Получить транзакцию по id")
    public TransactionDtoResponse getTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapToTransaction(transaction);
    }

    @LogExecutionTime(description = "Получить все транзакции, имеющие указанную категорию")
    public List<TransactionDtoResponse> getTransactionsByCategory(String categoryName){
        log.info("Поиск транзакций по категории: {}", categoryName);
        List<Transaction> transactionList = transactionRepository.findByCategoryName(categoryName);
        return transactionList.stream()
                .map(this::mapToTransaction)
                .toList();
    }

    @LogExecutionTime(description = "Получить все транзакции, имеющие указанную компанию")
    public List<TransactionDtoResponse> getTransactionsByCompany(String companyName){
        log.info("Поиск транзакций по компании: {}", companyName);
        List<Transaction> transactionList = transactionRepository.findByCompanyName(companyName);
        return transactionList.stream()
                .map(this::mapToTransaction)
                .toList();
    }

    private TransactionDtoResponse mapToTransaction(Transaction transaction){
        return TransactionDtoResponse.builder()
                .id(transaction.getId())
                .sum(transaction.getSum())
                .type(transaction.getType())
                .categoryName(transaction.getCategory().getName())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .userName(transaction.getUser().getUsername())
                .userId(transaction.getUser().getId())
                .build();
    }
}
