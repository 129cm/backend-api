package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.d129cm.backendapi.member.domain.Member;
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
            String orderSerial = "20240918-1234567";

            // when
            Order order = new Order(mockMember, orderSerial);

            // then
            assertThat(order.getMember()).isSameAs(mockMember);
            assertThat(order.getOrderSerial()).isSameAs(orderSerial);
        }
    }
}
