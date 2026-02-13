package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Controller.Documentation.AccountControllerDocumentation;
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
public class AccountController implements AccountControllerDocumentation {
    private final AccountService accountService;

    @Override
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        return ResponseEntity
                .created(URI.create("/account/" + account.getId()))
                .body(accountService.create(account));
    }

    @Override
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable Long accountId){
        return ResponseEntity
                .ok(accountService.getCurrentBalanceFromOnceCard(accountId));
    }

    @Override
    public ResponseEntity<String> subtractFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal subtract){
        BigDecimal newAmount = accountService.subtractFromAccount(accountId, subtract);
        return ResponseEntity
                .ok("Средства списаны, текущий баланс: " + newAmount + "р");
    }

    @Override
    public ResponseEntity<String> addToAccount(@PathVariable Long accountId, @RequestBody BigDecimal add){
        BigDecimal newAmount = accountService.addToAccount(accountId, add);
        return ResponseEntity
                .ok("Средства зачислены, текущий баланс: " + newAmount + "р");
    }

    @Override
    public ResponseEntity<Account> getAccountById(@PathVariable Long id){
        return ResponseEntity
                .ok(accountService.findById(id));
    }

    @Override
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity
                .ok(accountService.findAll());
    }

    @Override
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        accountService.delete(id);
        return ResponseEntity
                .ok("Счет успешно удален");
    }
}

