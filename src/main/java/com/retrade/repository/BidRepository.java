package com.retrade.repository;

import com.retrade.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    
    Page<Bid> findByAuctionIdOrderByAmountDesc(Long auctionId, Pageable pageable);
    
    @Query("SELECT b FROM Bid b WHERE b.auction.id = :auctionId ORDER BY b.amount DESC LIMIT 1")
    Optional<Bid> findHighestBidByAuctionId(@Param("auctionId") Long auctionId);
    
    @Query("SELECT b FROM Bid b WHERE b.bidder.id = :bidderId ORDER BY b.createdAt DESC")
    Page<Bid> findByBidderId(@Param("bidderId") Long bidderId, Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Bid b WHERE b.auction.id = :auctionId")
    Long countByAuctionId(@Param("auctionId") Long auctionId);
}
