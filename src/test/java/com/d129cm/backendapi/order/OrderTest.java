package com.d129cm.backendapi.order;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {

    @Nested
    class create {

        @Test
        void 생성성공_order_생성() {
            // given
            CommonCodeId mockId = mock(CommonCodeId.class);
            Member mockMember = mock(Member.class);
            String orderSerial = "주문번호";
            String orderGroupCode = GroupName.주문.getGroupId();

            when(mockId.getGroupId()).thenReturn(orderGroupCode);
            // when
            Order order = Order.builder()
                    .commonCodeId(mockId)
                    .orderSerial(orderSerial)
                    .member(mockMember)
                    .build();

            // then
            assertThat(order.getOrderSerial()).isEqualTo(orderSerial);
            assertThat(order.getMember()).isSameAs(mockMember);
            assertThat(order.getCommonCodeId()).isSameAs(mockId);
            assertThat(order.getCommonCodeId().getGroupId()).isEqualTo(orderGroupCode);
        }
    }
}
