package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.repository.OrderRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderManagerTest {

    @InjectMocks
    private OrderManager orderManager;

    @Mock
    private OrderRepository orderRepository;

    private static final int BACKNUMBER_LENGTH = 7;  // 36진법으로 7자리

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

    @Nested
    class createOrder {

        @Test
        void Order반환_order_생성() {
            // given
            Member member = MemberFixture.createMember("abc@example.com");
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);
            int FRONTNUMBER_LENGTH = "yyyyMMdd-".length();
            String orderSerial = "20240914-1234567";

            Order order = spy(Order.builder()
                    .member(member)
                    .commonCodeId(commonCodeId)
                    .build());
            order.setOrderSerial(orderSerial);

            when(orderRepository.existsByOrderSerial(any())).thenReturn(false);
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            // when
            Order result = orderManager.createOrder(member);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result.getMember()).isEqualTo(member);
                softly.assertThat(result.getCommonCodeId()).isEqualTo(commonCodeId);
                softly.assertThat(result.getOrderSerial()).isNotNull();
                softly.assertThat(result.getOrderSerial()).hasSize(FRONTNUMBER_LENGTH + BACKNUMBER_LENGTH);
                verify(orderRepository).save(any(Order.class));
            });
        }
    }
}
