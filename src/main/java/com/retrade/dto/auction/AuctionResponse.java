package com.retrade.dto.auction;

import com.retrade.dto.user.UserResponse;
import com.retrade.entity.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingBid;
    private BigDecimal highestBid;
    private Integer currentBids;
    private LocalDateTime endsAt;
    private String image;
    private String category;
    private String location;
    private Auction.Status status;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse seller;
    private List<ShippingOptionResponse> shippingOptions;
}