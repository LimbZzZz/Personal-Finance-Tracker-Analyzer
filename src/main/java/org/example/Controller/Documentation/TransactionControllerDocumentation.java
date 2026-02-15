package org.example.Controller.Documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.DTO.Request.TransactionDtoRequest;
import org.example.DTO.Response.TransactionDtoResponse;
import org.springframework.http.ResponseEntity;
import org.example.CustomException.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Транзакции", description = "Управление транзакциями")
public interface TransactionControllerDocumentation {
    @PostMapping("/create")
    @Operation(description = "Создать транзакцию")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
            description = "Успешное создание транзакции",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionDtoResponse.class)
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
                    )),
    })
    ResponseEntity<TransactionDtoResponse> createTransaction(@Valid @RequestBody TransactionDtoRequest request);

    @GetMapping("/get")
    @Operation(summary = "Получить список транзакций")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций получен")
    })
    ResponseEntity<List<TransactionDtoResponse>> getTransactions();

    @GetMapping("/get/{transactionId}")
    @Operation(summary = "Получить транзакцию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Транзакция успешно получена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Транзакция не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
    })
    ResponseEntity<TransactionDtoResponse> getTransactionByid(@PathVariable Long transactionId);

    @GetMapping("/getByCategory/{categoryName}")
    @Operation(summary = "Получить транзакцию по названию категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Транзакция успешно получена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Транзакция не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
    })
    ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCategoryName(@PathVariable String categoryName);

    @GetMapping("/getByCompany/{companyName}")
    @Operation(summary = "Получить транзакцию по названию компании")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Транзакция успешно получена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Транзакция не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
    })
    ResponseEntity<List<TransactionDtoResponse>> getTransactionsByCompanyName(@PathVariable String companyName);
}
