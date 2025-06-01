package com.retrade.dto.bid;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRequest {
    
    @NotNull(message = "입찰 금액은 필수입니다")
    @DecimalMin(value = "0.01", message = "입찰 금액은 0보다 커야 합니다")
    private BigDecimal amount;
}
