package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.repository.OrderItemOptionRepository;
import com.d129cm.backendapi.order.repository.OrderRepository;
import com.d129cm.backendapi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderManager {

    private static final String BASE36_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int BACKNUMBER_LENGTH = 7;  // 36진법으로 7자리
    private static final SecureRandom random = new SecureRandom();

    private final OrderRepository orderRepository;

    public OrdersSearchResultDto searchResult(String itemName, String startDate, String endDate, String orderState, int size, int page) {
        LocalDateTime startTime = Strings.isNotEmpty(startDate) ? LocalDateTime.parse(startDate) : LocalDateTime.of(1900, 1, 1, 0, 0);
        LocalDateTime endTime = Strings.isNotEmpty(endDate)
                ? LocalDateTime.parse(endDate)
                : LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()));

        size = Math.max(1, Math.min(size, 100));
        page = Math.max(0, page);

        return orderRepository.searchOrders(itemName, startTime, endTime, orderState, size, page);
    }

    public OrderDetailsDto getOrderDetailsByOrderId(Long orderId) {
        return orderRepository.findOrderDetailsByOrderId(orderId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NotFoundException::entityNotFound);
    }

    public Order createOrder(Member member) {
        String orderSerial;
        int retryCount = 0;
        do {
            orderSerial = generateFrontOrderSerial() + "-" + generateBackOrderSerial(BACKNUMBER_LENGTH);
            retryCount++;
            if (retryCount == 3) throw ConflictException.exceedMaxRetries("주문번호 생성");
        } while (orderRepository.existsByOrderSerial(orderSerial));

        Order order = new Order(member, orderSerial);
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

    public Page<Order> getOrdersByMemberId(Long memberId, Pageable pageable) {
        return orderRepository.findOrderByMemberId(memberId, pageable);
    }

    public Integer getAmountOfOrder(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders =  orderRepository.findOrdersByMemberIdBetween(memberId, startDate, endDate);
        Integer amountSpent = 0;

        for (Order order : orders) {
            // order 테이블에서 getTotalSalePrice 하기!
//            amountSpent += order;
        }
        return amountSpent;
    }
}
