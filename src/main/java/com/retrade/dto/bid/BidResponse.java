package com.retrade.dto.bid;

import com.retrade.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String user;
    private Bid.Status status;
}