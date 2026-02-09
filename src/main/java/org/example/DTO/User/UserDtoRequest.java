package org.example.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor
public class UserDtoRequest {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 20, message = "Имя пользователя должно бвть не меньше 4 и не больше 20 символов")
    private String name;
    @NotBlank(message = "email не может быть пустым")
    @Email
    private String email;
    @NotBlank(message = "У пользователя должен быть пароль")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password;
}
