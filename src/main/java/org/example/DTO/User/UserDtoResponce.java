package org.example.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDtoResponce {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer transactionCount;
}
