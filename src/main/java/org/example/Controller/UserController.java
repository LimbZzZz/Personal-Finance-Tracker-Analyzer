package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Controller.Documentation.UserControllerDocumentation;
import org.example.DTO.Request.UserDtoRequest;
import org.example.DTO.Response.UserDtoResponse;
import org.example.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDocumentation {
    private final UserService userService;

    @Override
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

    @Override
    public ResponseEntity<List<UserDtoResponse>> getUsers(){
        log.info("Входящий запрос на получение всех пользователей");
        return ResponseEntity
                .ok(userService.findAllUsers());
    }

    @Override
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id){
        log.info("Входящий запрос на получение пользователя " + id);
        return ResponseEntity
                .ok(userService.findUserById(id));
    }
}
