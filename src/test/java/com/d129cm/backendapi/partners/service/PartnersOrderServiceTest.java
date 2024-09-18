package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.service.MemberOrderService;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResponseDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PartnersOrderServiceTest {

    @InjectMocks
    private PartnersOrderService partnersOrderService;

    @Mock
    private OrderManager orderManager;

    @Nested
    class searchOrders {
        @Test
        void searchResult_성공() {
            // given
            OrdersSearchResponseDto order1 = new OrdersSearchResponseDto(1L, "User 1", LocalDateTime.now(), 1L, List.of());
            OrdersSearchResponseDto order2 = new OrdersSearchResponseDto(2L, "User 2", LocalDateTime.now(), 2L, List.of());
            OrdersSearchResultDto mockResult = new OrdersSearchResultDto(List.of(order1, order2), 2L);

            when(orderManager.searchResult(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                    .thenReturn(mockResult);

            // when
            OrdersSearchResultDto result = partnersOrderService.searchResult("item", "2023-01-01T00:00:00", "2023-12-31T23:59:59", "주문대기", 10, 0);

            // then
            assertEquals(2, result.getOrders().size());
            assertEquals(2L, result.getTotal());
            assertEquals(1L, result.getOrders().get(0).getOrderId());
            assertEquals(2L, result.getOrders().get(1).getOrderId());
        }
    }


    @Nested
    class getOrderDetailsByOrderId {
        @Test
        void OrderDetailsDto반환_orderId로_조회() {
            // given
            Long orderId = 1L;
            OrderDetailsDto dto = mock(OrderDetailsDto.class);
            when(dto.getOrderId()).thenReturn(orderId);
            when(orderManager.getOrderDetailsByOrderId(orderId)).thenReturn(dto);

            // when
            OrderDetailsDto detailsDto = partnersOrderService.getOrderDetailsByOrderId(orderId);

            // then
            verify(orderManager, times(1)).getOrderDetailsByOrderId(orderId);
            assertThat(detailsDto.getOrderId()).isEqualTo(orderId);
        }
    }
}
