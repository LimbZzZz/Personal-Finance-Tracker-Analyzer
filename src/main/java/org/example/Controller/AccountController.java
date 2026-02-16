package org.example.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Controller.Documentation.AccountControllerDocumentation;
import org.example.DTO.Request.AccountDtoRequest;
import org.example.DTO.Response.AccountDtoResponse;
import org.example.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.security.Principal;
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
        log.info("Запрос на получение текущего баланса у пользователя {} получен", accountId);
        BigDecimal currentAmount = accountService.getCurrentBalanceFromOnceCard(accountId);
        log.info("Текущий баланс пользователя {} успешно получен", accountId);
        return ResponseEntity
                .ok(currentAmount);
    }

    @Override
    public ResponseEntity<String> subtractFromAccount(@RequestBody BigDecimal subtract, Principal principal) {
        log.info("Запрос на списание суммы {}руб получен", subtract);
        BigDecimal newAmount = accountService.subtractFromAccount(subtract, principal.getName());
        log.info("Списание выполнено успешно, текущий баланс {}", newAmount);
        return ResponseEntity
                .ok("Средства списаны, текущий баланс: " + newAmount + "руб");
    }

    @Override
    public ResponseEntity<String> addToAccount(@RequestBody BigDecimal add, Principal principal){
        log.info("Запрос на зачисление суммы {}руб получен", add);
        BigDecimal newAmount = accountService.addToAccount(add, principal.getName());
        log.info("Зачисление выполнено успешно, текущий баланс {}", newAmount);
        return ResponseEntity
                .ok("Средства зачислены, текущий баланс: " + newAmount + "руб");
    }

    @Override
    public ResponseEntity<AccountDtoResponse> getAccountById(@PathVariable Long id){
        log.info("Запрос на получение счета по ID {}, получен", id);
        AccountDtoResponse account = accountService.findAccountById(id);
        log.info("Счет {} успешно получен", id);
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
        log.info("Запрос на изменение счета {} получен", id);
        accountService.changeActiveCard(id);
        log.info("Cчет {} успешно изменен", id);
        return ResponseEntity.ok("Счет успешно изменен");
    }

    @Override
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        log.info("Запрос на удаление счета {} получен", id);
        accountService.delete(id);
        log.info("Счет {} успешно удален", id);
        return ResponseEntity
                .ok("Счет успешно удален");
    }
}

