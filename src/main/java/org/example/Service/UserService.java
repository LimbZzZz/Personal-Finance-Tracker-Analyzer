package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.CustomException.EmailAlreadyExistException;
import org.example.CustomException.UserNotFoundException;
import org.example.Entity.User;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponse;
import org.example.Repository.UserRepository;
import org.example.Service.AOP.LogExecutionTime;
import org.example.Validator.UserValidator.UserBusinessValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserBusinessValidator validator;

    @LogExecutionTime(description = "Создание пользователя")
    public UserDtoResponse createUser(UserDtoRequest userRequest){
        validator.fullValidate(userRequest);
        checkEmailUnique(userRequest.getEmail());
        User newUser = createUserEntity(userRequest);
        User savedUser = userRepository.save(newUser);

        return mapToDtoResponse(savedUser);
    }

    private void checkEmailUnique(String email){
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistException();
        }
    }

    private User createUserEntity(UserDtoRequest request){
        User newUser = new User();
        newUser.setUsername(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        return newUser;
    }

    @LogExecutionTime(description = "Поиск всех пользователей")
    public List<UserDtoResponse> findAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDtoResponse)
                .collect(Collectors.toList());
    }

    @LogExecutionTime(description = "Поиск пользователя по ID")
    public UserDtoResponse findUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToDtoResponse(user);
    }

    public UserDtoResponse mapToDtoResponse(User user){
        UserDtoResponse dto = new UserDtoResponse();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if(user.getTransactionsList() != null){
            dto.setTransactionCount(user.getTransactionsList().size());
        }
        return dto;
    }
}
