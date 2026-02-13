package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Entity.Account;
import org.example.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        return ResponseEntity
                .created(URI.create("/account/" + account.getId()))
                .body(account);
    }

    @GetMapping("/getBalance/{accountId}")
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable Long accountId){
        return ResponseEntity
                .ok(accountService.getCurrentBalanceFromOnceCard(accountId));
    }

    @PostMapping("/subtract/{accountId}")
    public ResponseEntity<String> subtractFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal subtract){
        BigDecimal newAmount = accountService.subtractFromAccount(accountId, subtract);
        return ResponseEntity
                .ok("Средства списаны, текущий баланс: " + newAmount + "р");
    }

    @PostMapping("/add/{accountId}")
    public ResponseEntity<String> addToAccount(@PathVariable Long accountId, @RequestBody BigDecimal add){
        BigDecimal newAmount = accountService.addToAccount(accountId, add);
        return ResponseEntity
                .ok("Средства зачислены, текущий баланс: " + newAmount + "р");
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id){
        return ResponseEntity
                .ok(accountService.findById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity
                .ok(accountService.findAll());
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity
                .ok("Счет успешно удален");
    }
}

