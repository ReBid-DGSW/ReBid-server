package com.retrade.controller;

import com.retrade.dto.BidRequest;
import com.retrade.dto.BidResponse;
import com.retrade.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponse> placeBid(@Valid @RequestBody BidRequest request, Authentication auth) {
        try {
            BidResponse response = bidService.placeBid(request, auth.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<Page<BidResponse>> getBidsByAuction(
            @PathVariable Long auctionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("amount").descending());
        Page<BidResponse> bids = bidService.getBidsByAuction(auctionId, pageable);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/auction/{auctionId}/highest")
    public ResponseEntity<BidResponse> getHighestBid(@PathVariable Long auctionId) {
        Optional<BidResponse> bid = bidService.getHighestBid(auctionId);
        return bid.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/auction/{auctionId}/count")
    public ResponseEntity<Long> getBidCount(@PathVariable Long auctionId) {
        Long count = bidService.getBidCount(auctionId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<BidResponse>> getMyBids(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 현재 사용자 ID를 가져오는 로직이 필요합니다
        // 여기서는 간단히 1L로 설정했지만, 실제로는 auth에서 사용자 정보를 가져와야 합니다
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BidResponse> bids = bidService.getBidsByUser(1L, pageable);
        return ResponseEntity.ok(bids);
    }
}
