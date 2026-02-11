package org.example.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.CustomException.UserExceptions.EmailAlreadyExistException;
import org.example.CustomException.UserExceptions.UserNotFoundException;
import org.example.Entity.User;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponce;
import org.example.Repository.UserRepository;
import org.example.Validator.UserValidator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserValidator validatorController;

    public UserDtoResponce createUser(UserDtoRequest userRequest){
        validatorController.fullValidation(userRequest);
        checkEmailUnique(userRequest.getEmail());
        User newUser = createUserEntity(userRequest);
        User savedUser = userRepository.save(newUser);

        return mapToDtoResponce(savedUser);
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

    public List<UserDtoResponce> findAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDtoResponce)
                .collect(Collectors.toList());
    }

    public UserDtoResponce findUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToDtoResponce(user);
    }

    public UserDtoResponce mapToDtoResponce(User user){
        UserDtoResponce dto = new UserDtoResponce();
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
