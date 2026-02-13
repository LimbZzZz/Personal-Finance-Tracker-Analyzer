package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping("/create")
    public ResponseEntity<TransactionDtoResponse> createTransaction(@Valid @RequestBody TransactionDtoRequest request){
        TransactionDtoResponse createdTransaction = transactionService.createTransaction(request);
        return ResponseEntity
                .created(URI.create("api/transaction/" + createdTransaction.getId()))
                .body(createdTransaction);
    }

    @GetMapping("/get")
    public ResponseEntity<List<TransactionDtoResponse>> getTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/get/{transactionId}")
    public ResponseEntity<TransactionDtoResponse> getTransactionByid(@PathVariable Long transactionId){
        return ResponseEntity
                .ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/getByCategory/{categoryName}")
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCategoryName(@PathVariable String categoryName){
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCategory(categoryName);
        return ResponseEntity
                .ok(responces);
    }

    @GetMapping("/getByCompany/{companyName}")
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCompanyName(@PathVariable String companyName){
        List<TransactionDtoResponse> responces = transactionService.getTransactionsByCompany(companyName);
        return ResponseEntity
                .ok(responces);
    }
}
