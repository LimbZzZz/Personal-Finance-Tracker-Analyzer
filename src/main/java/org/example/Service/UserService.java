package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.CustomException.UserNotFoundException;
import org.example.Entity.User;
import org.example.DTO.User.UserDtoRequest;
import org.example.DTO.User.UserDtoResponce;
import org.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
   /* private final PasswordEncoder passwordEncoder;*/

    @Autowired
    public UserService(UserRepository userRepository /*PasswordEncoder passwordEncoder*/){
        this.userRepository = userRepository;
        /*this.passwordEncoder = passwordEncoder;*/
    }

    public UserDtoResponce createUser(UserDtoRequest userRequest){
        User newUser = new User();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        if(userRequest.getPassword() != null){
            newUser.setPassword(
                    userRequest.getPassword()
            );
        }
        User savedUser = userRepository.save(newUser);
        return mapToDtoResponce(savedUser);
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
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if(user.getTransactionsList() != null){
            dto.setTransactionCount(user.getTransactionsList().size());
        }
        return dto;
    }
}
