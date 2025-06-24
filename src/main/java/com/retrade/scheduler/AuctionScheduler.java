package com.retrade.scheduler;

import com.retrade.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionService auctionService;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void processExpiredAuctions() {
        try {
            auctionService.processExpiredAuctions();
            log.debug("만료된 경매 처리 완료");
        } catch (Exception e) {
            log.error("만료된 경매 처리 중 오류 발생", e);
        }
    }
}
