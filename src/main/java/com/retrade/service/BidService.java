package com.retrade.service;

import com.retrade.dto.BidRequest;
import com.retrade.dto.BidResponse;
import com.retrade.entity.Auction;
import com.retrade.entity.Bid;
import com.retrade.entity.User;
import com.retrade.repository.AuctionRepository;
import com.retrade.repository.BidRepository;
import com.retrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public BidResponse placeBid(BidRequest request, String userEmail) {
        User bidder = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new RuntimeException("경매를 찾을 수 없습니다."));

        // 경매 상태 체크
        if (!"ACTIVE".equals(auction.getStatus())) {
            throw new RuntimeException("활성화된 경매가 아닙니다.");
        }

        // 경매 종료 시간 체크
        if (auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("경매가 종료되었습니다.");
        }

        // 자신의 상품에 입찰하는지 체크
        if (auction.getProduct().getSeller().getId().equals(bidder.getId())) {
            throw new RuntimeException("자신의 상품에는 입찰할 수 없습니다.");
        }

        // 현재 최고가보다 높은지 체크
        if (request.getAmount().compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("현재 최고가보다 높은 금액을 입찰해야 합니다.");
        }

        // 입찰 생성
        Bid bid = Bid.builder()
                .auction(auction)
                .bidder(bidder)
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        Bid savedBid = bidRepository.save(bid);

        // 경매의 현재 가격 업데이트
        auction.setCurrentPrice(request.getAmount());
        auction.setUpdatedAt(LocalDateTime.now());
        auctionRepository.save(auction);

        return BidResponse.from(savedBid);
    }

    @Transactional(readOnly = true)
    public Page<BidResponse> getBidsByAuction(Long auctionId, Pageable pageable) {
        Page<Bid> bids = bidRepository.findByAuctionIdOrderByAmountDesc(auctionId, pageable);
        return bids.map(BidResponse::from);
    }

    @Transactional(readOnly = true)
    public Optional<BidResponse> getHighestBid(Long auctionId) {
        Optional<Bid> bid = bidRepository.findHighestBidByAuctionId(auctionId);
        return bid.map(BidResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<BidResponse> getBidsByUser(Long userId, Pageable pageable) {
        Page<Bid> bids = bidRepository.findByBidderId(userId, pageable);
        return bids.map(BidResponse::from);
    }

    @Transactional(readOnly = true)
    public Long getBidCount(Long auctionId) {
        return bidRepository.countByAuctionId(auctionId);
    }
}
