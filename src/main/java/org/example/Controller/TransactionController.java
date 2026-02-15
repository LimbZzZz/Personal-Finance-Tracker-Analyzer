package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Controller.Documentation.TransactionControllerDocumentation;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.DTO.Response.TransactionDtoResponse;
import org.example.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController implements TransactionControllerDocumentation {
    private final TransactionService transactionService;


    @Override
    public ResponseEntity<TransactionDtoResponse> createTransaction(@Valid @RequestBody TransactionDtoRequest request){
        log.info("Входящий запрос на создание транзакции Type: {}, Sum: {}", request.getType(), request.getSum());
        TransactionDtoResponse createdTransaction = transactionService.createTransaction(request);
        log.info("Тразнакция успешно создана");
        return ResponseEntity
                .created(URI.create("api/transaction/" + createdTransaction.getId()))
                .body(createdTransaction);
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactions(){
        log.info("Входящий запрос на получение списка транзакций");
        List<TransactionDtoResponse> dtoResponses = transactionService.getAllTransactions();
        log.info("Список транзакций успешно получен");
        return ResponseEntity.ok(dtoResponses);
    }

    @Override
    public ResponseEntity<TransactionDtoResponse> getTransactionByid(@PathVariable Long transactionId){
        log.info("Входящий запрос на получение транзакции по ID: {}", transactionId);
        TransactionDtoResponse dtoResponse = transactionService.getTransactionById(transactionId);
        log.info("Транзакция {} успешно получена", transactionId);
        return ResponseEntity
                .ok(dtoResponse);
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCategoryName(@PathVariable String categoryName){
        log.info("Запрос на получение списка транзакций с категорией {}", categoryName);
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCategory(categoryName);
        log.info("Список транзакций с категорией {} успешно получен", categoryName);
        return ResponseEntity
                .ok(responces);
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCompanyName(@PathVariable String companyName){
        log.info("Запрос на получение списка транзакций с компанией {}", companyName);
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCompany(companyName);
        log.info("Список транзакций с компанией {} успешно получен", companyName);
        return ResponseEntity
                .ok(responces);
    }
}
