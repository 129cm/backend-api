package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.manager.MemberManager;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderManager {

    private final OrderRepository orderRepository;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NotFoundException::entityNotFound);
    }

    public Order createOrder(CreateOrderDto createOrderDto, Member member) {

        Order order =  Order.builder()
                .member(member)
                // #1 빌더로 한번에 입력하기
                .orderSerial(generateOrderSerial(member))
                .commonCodeId(new CommonCodeId(CodeName.주문대기))
                .build();
        Order savedOrder = orderRepository.save(order);
        // #2 order 저장 후 toss order id 저장하기
        savedOrder.setTossOrderId(generateTossOrderId());
        return order;
    }

    private String generateTossOrderId() {
    }

    private String generateOrderSerial(Member member) {
        String nowToString = DateTimeFormatter.ofPattern("yyMMddHHmm").format(LocalDateTime.now());
        int memberHashCode = member.getName().hashCode(); // uuid random 을 뒤에 붙이기
        String orderSerial = nowToString + memberHashCode;
        return orderSerial;
    }

    public Order getOrderByTossOrderId(String tossOrderId) {
        return orderRepository.findByTossOrderId(tossOrderId).orElseThrow(NotFoundException::entityNotFound);
    }
}
