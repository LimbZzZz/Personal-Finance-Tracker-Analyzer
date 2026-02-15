package org.example.DTO.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDtoResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer transactionCount;
}
