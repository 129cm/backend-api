package com.d129cm.backendapi.order.service;

import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderManager orderManager;
    private final OrderItemOptionManager orderItemOptionManager;

    public OrdersSearchResultDto searchResult(String itemName, String startDate, String endDate, String orderState, int size, int page) {
        return orderManager.searchResult(itemName, startDate, endDate, orderState, size, page);
    }

    public OrderDetailsDto getOrderDetailsByOrderId(Long orderId) {
        return orderManager.getOrderDetailsByOrderId(orderId);
    }

    public String createOrder(CreateOrderDto createOrderDto, Member member) {
        Order order = orderManager.createOrder(member);
        orderItemOptionManager.createOrderItemOption(order, createOrderDto);
        return order.getOrderSerial();
    }
}