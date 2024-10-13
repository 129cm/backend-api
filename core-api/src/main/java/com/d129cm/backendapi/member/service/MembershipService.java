package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.order.manager.OrderManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipService {
    private final OrderManager orderManager;

    public Integer getAmountOfOrder(Long memberId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);
        return orderManager.getAmountOfOrder(memberId, startDate, endDate);
    }
}
