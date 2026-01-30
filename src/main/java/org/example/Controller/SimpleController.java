package org.example.Controller;

import jakarta.websocket.server.PathParam;
import org.apache.catalina.User;
import org.example.DTO.Transaction.TransactionRequest;
import org.example.DTO.Transaction.TransactionResponce;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponce;
import org.example.Entity.Transaction;
import org.example.Service.TransactionService;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<UserDtoResponce> createUser(@RequestBody UserDtoRequest userRequest){
        UserDtoResponce createdUser = userService.createUser(userRequest);
        return ResponseEntity
                .created(URI.create("/create/user/" + createdUser.getId()))
                .body(createdUser);

    }

    @PostMapping("/create/transaction")
    public ResponseEntity<TransactionResponce> createTransaction(@RequestBody TransactionRequest request){
        TransactionResponce createdTransaction = transactionService.createTransaction(request);
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
    public ResponseEntity<List<TransactionResponce>> getTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionResponce> getTransactionByid(@PathVariable Long id){
        return ResponseEntity
                .ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<UserDtoResponce> getUserById(@PathParam("id") Long id){
        return ResponseEntity
                .ok(userService.findUserById(id));
    }
}
