package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Controller.Documentation.TransactionControllerDocumentation;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.DTO.Transaction.TransactionDtoResponse;
import org.example.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController implements TransactionControllerDocumentation {
    private final TransactionService transactionService;


    @Override
    public ResponseEntity<TransactionDtoResponse> createTransaction(@Valid @RequestBody TransactionDtoRequest request){
        TransactionDtoResponse createdTransaction = transactionService.createTransaction(request);
        return ResponseEntity
                .created(URI.create("api/transaction/" + createdTransaction.getId()))
                .body(createdTransaction);
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @Override
    public ResponseEntity<TransactionDtoResponse> getTransactionByid(@PathVariable Long transactionId){
        return ResponseEntity
                .ok(transactionService.getTransactionById(transactionId));
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCategoryName(@PathVariable String categoryName){
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCategory(categoryName);
        return ResponseEntity
                .ok(responces);
    }

    @Override
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCompanyName(@PathVariable String companyName){
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCompany(companyName);
        return ResponseEntity
                .ok(responces);
    }
}
