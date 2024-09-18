package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.manager.OrderManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnersOrderService {

    private final OrderManager orderManager;

    public OrdersSearchResultDto searchResult(String itemName, String startDate, String endDate, String orderState, int size, int page) {
        return orderManager.searchResult(itemName, startDate, endDate, orderState, size, page);
    }

    public OrderDetailsDto getOrderDetailsByOrderId(Long orderId) {
        return orderManager.getOrderDetailsByOrderId(orderId);
    }
}
