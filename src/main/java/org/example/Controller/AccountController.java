package org.example.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Controller.Documentation.AccountControllerDocumentation;
import org.example.DTO.Request.AccountDtoRequest;
import org.example.DTO.Response.AccountDtoResponse;
import org.example.Entity.Account;
import org.example.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController implements AccountControllerDocumentation {
    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountDtoResponse> createAccount(@RequestBody AccountDtoRequest request){
        log.info("Запрос на создание счета {} получен", request.getCardNumber());
        AccountDtoResponse response = accountService.createAccount(request);
        log.info("Счет {} успешно создан", request.getCardNumber());
        return ResponseEntity.created(URI.create("api/account/create/" + response.getCardNumber()))
                .body(response);
    }

    @Override
    public ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable Long accountId){
        log.info("Запрос на получение текущего баланса получен");
        BigDecimal currentAmount = accountService.getCurrentBalanceFromOnceCard(accountId);
        log.info("Текущий баланс успешно получен");
        return ResponseEntity
                .ok(currentAmount);
    }

    @Override
    public ResponseEntity<String> subtractFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal subtract){
        log.info("Запрос на списание суммы {}руб, со счета {}, получен", subtract, accountId);
        BigDecimal newAmount = accountService.subtractFromAccount(subtract);
        log.info("Списание выполнено успешно");
        return ResponseEntity
                .ok("Средства списаны, текущий баланс: " + newAmount + "руб");
    }

    @Override
    public ResponseEntity<String> addToAccount(@PathVariable Long accountId, @RequestBody BigDecimal add){
        log.info("Запрос на зачисление суммы {}руб, на счет {}, получен", add, accountId);
        BigDecimal newAmount = accountService.addToAccount(add);
        log.info("Зачисление выполнено успешно");
        return ResponseEntity
                .ok("Средства зачислены, текущий баланс: " + newAmount + "руб");
    }

    @Override
    public ResponseEntity<Account> getAccountById(@PathVariable Long id){
        log.info("Запрос на получение счета по ID, получен");
        Account account = accountService.findById(id);
        log.info("Счет успешно получен");
        return ResponseEntity
                .ok(account);
    }

    @Override
    public ResponseEntity<List<AccountDtoResponse>> getAllAccounts(){
        log.info("Запрос на получение списка счетов получен");
        log.info("Список счетов успешно получен");
        return ResponseEntity
                .ok(accountService.findAllAccounts());
    }

    @Override
    public ResponseEntity<String> changeActiveAccount(Long id){
        log.info("Запрос на изменение счета получен");
        accountService.changeActiveCard(id);
        return ResponseEntity.ok("Счет успешно изменен");
    }

    @Override
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        log.info("Запрос на удаление счета {} получен", id);
        accountService.delete(id);
        return ResponseEntity
                .ok("Счет успешно удален");
    }
}

