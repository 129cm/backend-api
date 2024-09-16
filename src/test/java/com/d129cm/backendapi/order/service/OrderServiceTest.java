package com.d129cm.backendapi.order.service;

import com.d129cm.backendapi.order.dto.OrdersSearchResponseDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.manager.OrderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderManager orderManager;

    @InjectMocks
    private OrderService orderService;

    @Test
    void searchResult_성공() {
        // given
        OrdersSearchResponseDto order1 = new OrdersSearchResponseDto(1L, "User 1", LocalDateTime.now(), 1L, List.of(), "Completed");
        OrdersSearchResponseDto order2 = new OrdersSearchResponseDto(2L, "User 2", LocalDateTime.now(), 2L, List.of(), "Pending");
        OrdersSearchResultDto mockResult = new OrdersSearchResultDto(List.of(order1, order2), 2L);

        when(orderManager.searchResult(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(mockResult);

        // when
        OrdersSearchResultDto result = orderService.searchResult("item", "2023-01-01T00:00:00", "2023-12-31T23:59:59", "Completed", 10, 0);

        // then
        assertEquals(2, result.getOrders().size());
        assertEquals(2L, result.getTotal());
        assertEquals(1L, result.getOrders().get(0).getOrderId());
        assertEquals(2L, result.getOrders().get(1).getOrderId());
    }
}
