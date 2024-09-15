package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.repository.OrderItemOptionRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemOptionManagerTest {

    @InjectMocks
    private OrderItemOptionManager orderItemOptionManager;

    @Mock
    private OrderItemOptionRepository orderItemOptionRepository;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Nested
    class getOrderItemOptionByOrderId {

        @Test
        void List_OrderItemOption_반환_orderI로_조회() {
            // given
            Long orderId = 1L;
            List<OrderItemOption> orderItemOptionList = new ArrayList<>();
            orderItemOptionList.add(mock(OrderItemOption.class));

            when(orderItemOptionRepository.findByOrderId(orderId)).thenReturn(orderItemOptionList);

            // when
            List<OrderItemOption> result = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);

            // then
            verify(orderItemOptionRepository).findByOrderId(orderId);

        }
    }
}
