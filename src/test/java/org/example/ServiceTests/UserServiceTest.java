package org.example.ServiceTests;

import org.example.CustomException.EmailAlreadyExistException;
import org.example.DTO.Request.UserDtoRequest;
import org.example.DTO.Response.UserDtoResponse;
import org.example.Entity.User;
import org.example.Repository.UserRepository;
import org.example.Service.UserService;
import org.example.Validator.UserValidator.UserBusinessValidator;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.ValidationException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserBusinessValidator validator;
    @InjectMocks
    private UserService userService;
    private UserDtoRequest validRequest;
    private User savedUser;

    @BeforeEach
    void initialize(){
        validRequest = UserDtoRequest.builder()
                .name("SomeName")
                .email("SomeEmail@mail.ru")
                .password("SomePassword")
                .build();

        savedUser = User.builder()
                .id(1L)
                .username("SomeName")
                .email("SomeEmail@mail.ru")
                .password("SomePassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    @Test
    public void createUser_ShouldSaveUser_WhenDataIsValid(){
        when(userRepository.existsByEmail("SomeEmail@mail.ru"))
                .thenReturn(false);
        when(passwordEncoder.encode("SomePassword"))
                .thenReturn("encode_password");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserDtoResponse responce = userService.createUser(validRequest);

        assertThat(responce).isNotNull();
        assertThat(responce.getId()).isEqualTo(1L);
        assertThat(responce.getName()).isEqualTo("SomeName");
        assertThat(responce.getEmail()).isEqualTo("SomeEmail@mail.ru");

        verify(userRepository).existsByEmail("SomeEmail@mail.ru");
        verify(passwordEncoder).encode("SomePassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void createUser_ShouldThrowException_WhenInputInvalid(){
        doThrow(new ValidationException("Invalid email"))
                .when(validator).fullValidate(any(UserDtoRequest.class));

        List<UserDtoRequest> invalidRequest = List.of(
                new UserDtoRequest("name", "", "password"),
                new UserDtoRequest("name", "invalid", "password"),
                new UserDtoRequest("name", "test@mail.ru", ""),
                new UserDtoRequest("name", "test@mail.ru", "123"),
                new UserDtoRequest("", "test@mail.ru", "password123"),
                new UserDtoRequest(null, null, null)
        );

        for(UserDtoRequest request : invalidRequest){
            assertThatThrownBy(() -> userService.createUser(request))
                    .isInstanceOf(ValidationException.class);
        }

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_ShouldThrowException_whenEmailAlreadyExists(){
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(true);

        UserDtoRequest request = new UserDtoRequest("name", "test@mail.ru", "password");

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    public void createUser_ShouldThrowException_WhenRepositoryFails(){
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenThrow(new ServiceException("Database save failed"));

        assertThatThrownBy(() -> userService.createUser(validRequest)).isInstanceOf(ServiceException.class);
    }

    @Test
    public void findAllUsers_ShouldViewAllUsers_WhenRepositoryNotEmpty(){
        User user1 = new User().builder()
                .id(1L)
                .username("name1")
                .email("email1@mail.ru")
                .password("password1")
                .build();
        User user2 = new User().builder()
                .id(2L)
                .username("name2")
                .email("email2@mail.ru")
                .password("password2")
                .build();
        List<User> userList = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDtoResponse> responce = userService.findAllUsers();

        assertThat(responce)
                .hasSize(2)
                .extracting(UserDtoResponse::getId, UserDtoResponse::getName, UserDtoResponse::getEmail)
                .containsExactly(
                        tuple(1L, "name1", "email1@mail.ru"),
                        tuple(2L, "name2", "email2@mail.ru")
                );

        verify(userRepository).findAll();
    }

    @Test
    public void findAllUsers_ShouldThrowException_WhenRepositoryFails(){
        when(userRepository.findAll())
                .thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> userService.findAllUsers())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB error");

        verify(userRepository).findAll();
    }
}
