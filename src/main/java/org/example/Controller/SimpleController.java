package org.example.Controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.example.DTO.Transaction.TransactionDtoRequest;
import org.example.DTO.Transaction.TransactionDtoResponce;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponce;
import org.example.Enum.CategoryType;
import org.example.Service.TransactionService;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SimpleController {
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public SimpleController(UserService userService, TransactionService transactionService){
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @PostMapping("/create/user")
    public ResponseEntity<UserDtoResponce> createUser(@Valid @RequestBody UserDtoRequest userRequest){
        UserDtoResponce createdUser = userService.createUser(userRequest);
        return ResponseEntity
                .created(URI.create("/create/user/" + createdUser.getId()))
                .body(createdUser);

    }

    @PostMapping("/create/transaction")
    public ResponseEntity<TransactionDtoResponce> createTransaction(@Valid @RequestBody TransactionDtoRequest request){
        TransactionDtoResponce createdTransaction = transactionService.createTransaction(request);
        return ResponseEntity
                .created(URI.create("/create/transaction/" + createdTransaction.getId()))
                .body(createdTransaction);
    }

    @GetMapping("/get/users")
    public ResponseEntity<List<UserDtoResponce>> getUsers(){
        return ResponseEntity
                .ok(userService.findAllUsers());
    }

    @GetMapping("/get/transactions")
    public ResponseEntity<List<TransactionDtoResponce>> getTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionDtoResponce> getTransactionByid(@PathVariable Long id){
        return ResponseEntity
                .ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<UserDtoResponce> getUserById(@PathVariable Long id){
        return ResponseEntity
                .ok(userService.findUserById(id));
    }

    @GetMapping("/transaction/getByCategory/{category}")
    public ResponseEntity<List<TransactionDtoResponce>> getTransactionByCategory(@PathVariable CategoryType category){
        List<TransactionDtoResponce> responces = new ArrayList<>();
        responces = transactionService.getTransactionByCategory(category);
        return ResponseEntity
                .ok(responces);
    }
}
