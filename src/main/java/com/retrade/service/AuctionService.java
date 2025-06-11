package com.retrade.service;

import com.retrade.dto.AuctionResponse;
import com.retrade.entity.Auction;
import com.retrade.entity.Product;
import com.retrade.repository.AuctionRepository;
import com.retrade.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    public AuctionResponse createAuction(Long productId, BigDecimal startingPrice, LocalDateTime endTime, String userEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 권한 체크
        if (!product.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("경매를 생성할 권한이 없습니다.");
        }

        Auction auction = Auction.builder()
                .product(product)
                .startingPrice(startingPrice)
                .currentPrice(startingPrice)
                .endTime(endTime)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Auction savedAuction = auctionRepository.save(auction);
        return AuctionResponse.from(savedAuction);
    }

    @Transactional(readOnly = true)
    public AuctionResponse getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("경매를 찾을 수 없습니다."));
        return AuctionResponse.from(auction);
    }

    @Transactional(readOnly = true)
    public Page<AuctionResponse> getAuctionsByProduct(Long productId, Pageable pageable) {
        Page<Auction> auctions = auctionRepository.findByProductId(productId, pageable);
        return auctions.map(AuctionResponse::from);
    }

    @Transactional(readOnly = true)
    public List<AuctionResponse> getActiveAuctions() {
        List<Auction> auctions = auctionRepository.findActiveAuctions(LocalDateTime.now());
        return auctions.stream().map(AuctionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<AuctionResponse> getAuctionsBySeller(Long sellerId, Pageable pageable) {
        Page<Auction> auctions = auctionRepository.findBySellerId(sellerId, pageable);
        return auctions.map(AuctionResponse::from);
    }

    public void endAuction(Long id, String userEmail) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("경매를 찾을 수 없습니다."));

        // 권한 체크
        if (!auction.getProduct().getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("경매를 종료할 권한이 없습니다.");
        }

        auction.setStatus("ENDED");
        auction.setUpdatedAt(LocalDateTime.now());
        auctionRepository.save(auction);
    }

    // 만료된 경매들을 자동으로 종료하는 스케줄러용 메서드
    public void processExpiredAuctions() {
        List<Auction> expiredAuctions = auctionRepository.findExpiredAuctions(LocalDateTime.now());
        for (Auction auction : expiredAuctions) {
            auction.setStatus("ENDED");
            auction.setUpdatedAt(LocalDateTime.now());
        }
        auctionRepository.saveAll(expiredAuctions);
    }
}
