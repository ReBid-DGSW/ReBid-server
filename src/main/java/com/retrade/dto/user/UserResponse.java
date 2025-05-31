package com.retrade.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private Double rating;
    private Integer salesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}