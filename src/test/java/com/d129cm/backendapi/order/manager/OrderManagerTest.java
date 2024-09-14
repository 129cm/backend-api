package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.repository.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderManagerTest {

    @InjectMocks
    private OrderManager orderManager;

    @Mock
    private OrderRepository orderRepository;

    private static final String BASE36_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int BACKNUMBER_LENGTH = 7;  // 36진법으로 7자리
    private static final Random random = new Random();

    @Nested
    class getOrderById {

        @Test
        void Order반환_id로_조회() {
            // given
            Long orderId = 1L;
            CommonCodeId oldCommonCodeId = new CommonCodeId(CodeName.주문대기);
            Member mockMember = mock(Member.class);
            Order order = Order.builder()
                    .commonCodeId(oldCommonCodeId)
                    .member(mockMember)
                    .build();
            when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

            // when
            Order result = orderManager.getOrderById(orderId);

            // then
            assertThat(result).isEqualTo(order);
        }
    }
}
