package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class OrderManager {

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

}
