package org.example.Controller.Documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.CustomException.ErrorResponse;
import org.example.DTO.Request.AccountDtoRequest;
import org.example.DTO.Response.AccountDtoResponse;
import org.example.Entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Tag(name = "Счет", description = "Управление счетами")
public interface AccountControllerDocumentation {
    @PostMapping("/create")
    @Operation(summary = "Создание счета")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
            description = "Счет успешно создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountDtoResponse.class)
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
    ResponseEntity<AccountDtoResponse> createAccount(@RequestBody AccountDtoRequest request);

    @PostMapping("/change")
    @Operation(summary = "Изменение счета для оплаты/зачислений")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Счет успешно изменен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class)
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
    ResponseEntity<String> changeActiveAccount(Long id);
    @GetMapping("/getBalance/{accountId}")
    @Operation(summary = "Получение текущего баланса")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Текущий баланс успешно получен",
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
    @Operation(summary = "Списание с текущего активного счета")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Списание со счета выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось списать сумму со счета",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    public ResponseEntity<String> subtractFromAccount(@RequestBody BigDecimal subtract, Principal principal);


    @PostMapping("/add/{accountId}")
    @Operation(summary = "Зачисление на текущий активный счет")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Зачисление на счет выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось зачислить сумму на счет",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> addToAccount(@RequestBody BigDecimal add, Principal principal);


    @GetMapping("/getById/{id}")
    @Operation(summary = "Получение счета по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Получение счета по ID выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Не удалось получить счет по ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<AccountDtoResponse> getAccountById(@PathVariable Long id);


    @GetMapping("/get/all")
    @Operation(summary = "Получение всех счетов пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех счетов выполнено успешно")
    })
    ResponseEntity<List<AccountDtoResponse>> getAllAccounts();


    @GetMapping("/delete/{id}")
    @Operation(summary = "Удаление счета по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Удаление счета выполнено успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class)
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
