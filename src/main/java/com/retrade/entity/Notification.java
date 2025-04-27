package com.retrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    private Long relatedId; // 관련된 경매, 상품 등의 ID
    
    @Builder.Default
    private Boolean isRead = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    public enum NotificationType {
        BID_PLACED,      // 입찰이 들어왔을 때
        BID_OUTBID,      // 내 입찰이 밀렸을 때
        AUCTION_ENDING,  // 경매 종료 임박
        AUCTION_WON,     // 경매 낙찰
        AUCTION_LOST,    // 경매 낙찰 실패
        PAYMENT_DUE,     // 결제 필요
        ITEM_SHIPPED     // 상품 발송
    }
}
