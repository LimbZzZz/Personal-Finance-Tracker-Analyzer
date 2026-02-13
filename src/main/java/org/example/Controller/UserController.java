package org.example.Controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponse;
import org.example.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest userRequest){
        log.info("Входящий запрос на создание пользователя: Username: {}, Email: {}",
                userRequest.getName(),
                userRequest.getEmail());

            UserDtoResponse createdUser = userService.createUser(userRequest);
            log.info("Пользователь {} успешно создан", userRequest.getEmail());

            return ResponseEntity
                    .created(URI.create("/create/user/" + createdUser.getId()))
                    .body(createdUser);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserDtoResponse>> getUsers(){
        log.info("Входящий запрос на получение всех пользователей");
        return ResponseEntity
                .ok(userService.findAllUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id){
        log.info("Входящий запрос на получение пользователя " + id);
        return ResponseEntity
                .ok(userService.findUserById(id));
    }
}
