package com.retrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "auctions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "starting_bid", nullable = false, precision = 10, scale = 2)
    private BigDecimal startingBid;
    
    @Column(name = "highest_bid", precision = 10, scale = 2)
    private BigDecimal highestBid;
    
    @Column(name = "current_bids")
    @Builder.Default
    private Integer currentBids = 0;
    
    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;
    
    private String image;
    
    @Column(nullable = false)
    private String category;
    
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;
    
    @Builder.Default
    private Integer views = 0;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids;
    
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<ShippingOption> shippingOptions;
    
    public enum Status {
        ACTIVE, ENDED, CANCELLED
    }
}