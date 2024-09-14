package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OrderManager {

    private static final String BASE36_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int BACKNUMBER_LENGTH = 7;  // 36진법으로 7자리
    private static final Random random = new Random();

    private final OrderRepository orderRepository;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NotFoundException::entityNotFound);
    }

    public Order createOrder(Member member) {
        Order order = Order.builder()
                .member(member)
                .commonCodeId(new CommonCodeId(CodeName.주문대기))
                .build();

        String orderSerial = "";
        int retryCount = 0;
        do {
            orderSerial = generateFrontOrderSerial() + "-" + generateBackOrderSerial(BACKNUMBER_LENGTH);;
            retryCount++;
            if (retryCount == 3) throw ConflictException.exceedMaxRetries("주문번호 생성");
        } while (orderRepository.existsByOrderSerial(orderSerial));
        order.setOrderSerial(orderSerial);
        return orderRepository.save(order);
    }

    private String generateFrontOrderSerial() {
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private static String generateBackOrderSerial(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE36_CHARS.charAt(random.nextInt(36)));
        }
        return sb.toString();
    }

    public Order getOrderByOrderSerial(String OrderSerial) {
        return orderRepository.findByOrderSerial(OrderSerial).orElseThrow(NotFoundException::entityNotFound);
    }
}
