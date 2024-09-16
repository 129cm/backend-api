package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;

import java.time.LocalDateTime;

public interface OrderQueryRepository {
    OrdersSearchResultDto searchOrders(String itemName, LocalDateTime startDate, LocalDateTime endDate, String orderState, int size, int page);
}
