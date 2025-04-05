package com.retrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    private String fullName;
    
    private String phoneNumber;
    
    private String address;
    
    private String profileImage;
    
    // OAuth2 관련 필드 추가
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    
    private String providerId;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum AuthProvider {
        LOCAL, GOOGLE, FACEBOOK
    }
}
