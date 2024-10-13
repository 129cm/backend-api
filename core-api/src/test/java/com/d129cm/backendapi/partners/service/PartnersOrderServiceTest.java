package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrderItemOptionIdDto;
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

@ExtendWith(MockitoExtension.class)
public class PartnersOrderServiceTest {

    @InjectMocks
    private PartnersOrderService partnersOrderService;

    @Mock
    private OrderManager orderManager;

    @Mock
    private OrderItemOptionManager orderItemOptionManager;

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

    @Nested
    class acceptOrders {

        @Test
        void 성공_주문_상태_주문완료로_변경() {
            // given
            OrderItemOptionIdDto orderItemOptionIdDto1 = mock(OrderItemOptionIdDto.class);
            OrderItemOptionIdDto orderItemOptionIdDto2 = mock(OrderItemOptionIdDto.class);
            List<OrderItemOptionIdDto> requests = List.of(orderItemOptionIdDto1, orderItemOptionIdDto2);

            OrderItemOptionId orderItemOptionId1 = new OrderItemOptionId(1L, 1L);
            OrderItemOptionId orderItemOptionId2 = new OrderItemOptionId(1L, 2L);

            when(orderItemOptionIdDto1.toOrderItemOptionId()).thenReturn(orderItemOptionId1);
            when(orderItemOptionIdDto2.toOrderItemOptionId()).thenReturn(orderItemOptionId2);
            when(orderItemOptionManager.modifyOrderState(eq(CodeName.주문완료.getCodeId()), any())).thenReturn(2);

            // when
            int updated = partnersOrderService.acceptOrders(requests);

            // then
            verify(orderItemOptionIdDto1, times(1)).toOrderItemOptionId();
            verify(orderItemOptionIdDto2, times(1)).toOrderItemOptionId();

            verify(orderItemOptionManager, times(1)).modifyOrderState(eq(CodeName.주문완료.getCodeId()), argThat(argument -> {
                List<OrderItemOptionId> expectedIds = List.of(orderItemOptionId1, orderItemOptionId2);
                return argument.equals(expectedIds);
            }));

            assertThat(updated).isEqualTo(2);
        }
    }

    @Nested
    class cancelOrder {

        @Test
        void 성공_주문_상태_주문취소로_변경() {
            // given
            OrderItemOptionIdDto orderItemOptionIdDto1 = mock(OrderItemOptionIdDto.class);
            OrderItemOptionIdDto orderItemOptionIdDto2 = mock(OrderItemOptionIdDto.class);
            List<OrderItemOptionIdDto> requests = List.of(orderItemOptionIdDto1, orderItemOptionIdDto2);

            OrderItemOptionId orderItemOptionId1 = new OrderItemOptionId(1L, 1L);
            OrderItemOptionId orderItemOptionId2 = new OrderItemOptionId(1L, 2L);

            when(orderItemOptionIdDto1.toOrderItemOptionId()).thenReturn(orderItemOptionId1);
            when(orderItemOptionIdDto2.toOrderItemOptionId()).thenReturn(orderItemOptionId2);
            when(orderItemOptionManager.modifyOrderState(eq(CodeName.주문취소.getCodeId()), any())).thenReturn(2);

            // when
            int updated = partnersOrderService.cancelOrders(requests);

            // then
            verify(orderItemOptionIdDto1, times(1)).toOrderItemOptionId();
            verify(orderItemOptionIdDto2, times(1)).toOrderItemOptionId();

            verify(orderItemOptionManager, times(1)).modifyOrderState(eq(CodeName.주문취소.getCodeId()), argThat(argument -> {
                List<OrderItemOptionId> expectedIds = List.of(orderItemOptionId1, orderItemOptionId2);
                return argument.equals(expectedIds);
            }));

            assertThat(updated).isEqualTo(2);
        }
    }
}
