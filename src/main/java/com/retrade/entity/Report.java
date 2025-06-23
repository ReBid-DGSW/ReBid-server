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
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Enumerated(EnumType.STRING)
    private ReportType type;
    
    @Column(nullable = false, length = 1000)
    private String reason;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    public enum ReportType {
        FAKE_PRODUCT,     // 가짜 상품
        INAPPROPRIATE,    // 부적절한 내용
        SPAM,            // 스팸
        FRAUD,           // 사기
        OTHER            // 기타
    }
    
    public enum ReportStatus {
        PENDING,         // 처리 대기
        REVIEWING,       // 검토 중
        RESOLVED,        // 해결됨
        REJECTED         // 거부됨
    }
}
