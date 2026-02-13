package org.example.Controller.Documentation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.CustomException.ErrorResponse;
import org.example.Entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Счет", description = "Управление счетами")
public interface AccountControllerDocumentation {
    @PostMapping("/create")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
            description = "Счет успешно создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)
            )),
            @ApiResponse(responseCode = "400",
                    description = "Ошибка валидации",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Account> createAccount(@RequestBody Account account);

    @GetMapping("/getBalance/{accountId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Текущий баланс успешно получени",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Ошибка получения счета",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<BigDecimal> getCurrentBalance(@PathVariable Long accountId);

    @PostMapping("/subtract/{accountId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Списание со счета выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось списать сумму со счета",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> subtractFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal subtract);

    @PostMapping("/add/{accountId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Зачисление на счет выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось зачислить сумму на счет",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> addToAccount(@PathVariable Long accountId, @RequestBody BigDecimal add);


    @GetMapping("/getById/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Получение счета по ID выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось получить счет по ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Account> getAccountById(@PathVariable Long id);


    @GetMapping("/get/all")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех счетов выполнено успешно")
    })
    ResponseEntity<List<Account>> getAllAccounts();


    @GetMapping("/delete/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Удаление счета выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось удалить счет",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> deleteAccount(@PathVariable Long id);
}
