package com.retrade.controller;

import com.retrade.dto.AuctionResponse;
import com.retrade.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestParam Long productId,
            @RequestParam BigDecimal startingPrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            Authentication auth) {
        try {
            AuctionResponse response = auctionService.createAuction(productId, startingPrice, endTime, auth.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuction(@PathVariable Long id) {
        try {
            AuctionResponse auction = auctionService.getAuctionById(id);
            return ResponseEntity.ok(auction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<AuctionResponse>> getAuctionsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuctionResponse> auctions = auctionService.getAuctionsByProduct(productId, pageable);
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AuctionResponse>> getActiveAuctions() {
        List<AuctionResponse> auctions = auctionService.getActiveAuctions();
        return ResponseEntity.ok(auctions);
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<Void> endAuction(@PathVariable Long id, Authentication auth) {
        try {
            auctionService.endAuction(id, auth.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
