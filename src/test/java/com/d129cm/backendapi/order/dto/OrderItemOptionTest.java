package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderItemOptionTest {

    @Nested
    class create {

        @Test
        void 생성성공_OrderItemOption_생성() {
            // given
            Order mockOrder = mock(Order.class);
            ItemOption mockItemOption = mock(ItemOption.class);
            int count = 10;
            int salesPrice = 10000;

            when(mockOrder.getId()).thenReturn(1L);
            when(mockItemOption.getId()).thenReturn(10L);

            // when
            OrderItemOption orderItemOption = OrderItemOption.builder()
                    .order(mockOrder)
                    .itemOption(mockItemOption)
                    .count(count)
                    .salesPrice(salesPrice)
                    .build();

            // then
            assertThat(orderItemOption).isNotNull();
            assertThat(orderItemOption.getId().getOrderId()).isEqualTo(1L);
            assertThat(orderItemOption.getId().getItemOptionId()).isEqualTo(10L);
            assertThat(orderItemOption.getOrder()).isEqualTo(mockOrder);
            assertThat(orderItemOption.getItemOption()).isEqualTo(mockItemOption);
            assertThat(orderItemOption.getCount()).isEqualTo(count);
            assertThat(orderItemOption.getSalesPrice()).isEqualTo(salesPrice);
        }
    }
}
