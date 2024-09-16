package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderManagerTest {

    @InjectMocks
    private OrderManager orderManager;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void 검색결과반환_검색어_없음_10개_조회() {
        // given
        int size = 10;
        int page = 0;
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        // when
        orderManager.searchResult(null, null, null, null, size, page);

        // then
        LocalDateTime endTime = LocalDateTime.of(today, LocalTime.of(nowTime.getHour(), nowTime.getMinute()));
        verify(orderRepository, times(1)).searchOrders(null, LocalDateTime.of(1900, 1, 1, 0, 0), endTime, null, size, page);
    }
}
