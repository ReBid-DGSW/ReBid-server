package com.retrade.repository;

import com.retrade.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    
    Page<Auction> findByProductId(Long productId, Pageable pageable);
    
    @Query("SELECT a FROM Auction a WHERE a.endTime > :now ORDER BY a.endTime ASC")
    List<Auction> findActiveAuctions(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Auction a WHERE a.endTime <= :now AND a.status = 'ACTIVE'")
    List<Auction> findExpiredAuctions(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Auction a WHERE a.product.seller.id = :sellerId")
    Page<Auction> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
}
