package com.d129cm.backendapi.order.service;

import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
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

    public String createOrder(CreateOrderDto createOrderDto, Member member) {
        Order order = orderManager.createOrder(createOrderDto, member);
        orderItemOptionManager.createOrderItemOption(order, createOrderDto);
        return order.getTossOrderId();
    }
}
