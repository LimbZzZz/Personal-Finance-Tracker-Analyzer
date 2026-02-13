package org.example.Controller.Documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponse;
import org.springframework.http.ResponseEntity;
import org.example.CustomException.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "пользователи", description = "Управление пользователями")
public interface UserControllerDocumentation {
    @PostMapping("/create")
    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class)
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
    ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest userRequest);

    @GetMapping("/get")
    @Operation(summary = "Получить список пользователей")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей получен")
    })
    ResponseEntity<List<UserDtoResponse>> getUsers();

    @GetMapping("/get/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Пользователь успешно получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class)
            )),
            @ApiResponse(responseCode = "404",
                    description = "Пользователь не найден",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
    })
    ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id);
}
