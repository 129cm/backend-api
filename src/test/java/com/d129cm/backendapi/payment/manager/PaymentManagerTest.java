package com.d129cm.backendapi.payment.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.fixture.ItemOptionFixture;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentManagerTest {

    @InjectMocks
    private PaymentManager paymentManager;

    @Mock
    private OrderItemOptionManager orderItemOptionManager;


    @Nested
    class getTotalPrice {

        @Test
        void 총금액반환_주문한_아이템_가격을_모두_합한_총_금액_조회() {
            // given
            Long orderId = 1L;
            Order order = mock(Order.class);
            ItemOption itemOption1 = ItemOptionFixture.createItemOption(mock(Item.class));
            ItemOption itemOption2 = ItemOptionFixture.createItemOption(mock(Item.class));

            when(order.getId()).thenReturn(orderId);

            Integer count1 = 3;
            Integer salePrice1 = 1000;
            Integer count2 = 2;
            Integer salePrice2 = 2000;

            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            OrderItemOption orderItemOption1 = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption1)
                    .count(count1)
                    .salesPrice(salePrice1)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .build();
            OrderItemOption orderItemOption2 = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption2)
                    .count(count2)
                    .salesPrice(salePrice2)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .build();
            orderItemOptions.add(orderItemOption1);
            orderItemOptions.add(orderItemOption2);

            when(orderItemOptionManager.getOrderItemOptionByOrderId(orderId)).thenReturn(orderItemOptions);

            // when
            Integer result = paymentManager.getTotalPrice(orderId);

            // then
            assertAll(
                    () -> assertThat(result).isEqualTo(salePrice1 + salePrice2),
                    () -> verify(orderItemOptionManager).getOrderItemOptionByOrderId(orderId)
            );
        }
    }
}
