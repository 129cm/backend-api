package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
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
            String orderGroupCode = GroupName.주문.getGroupId();

            when(mockId.getGroupId()).thenReturn(orderGroupCode);
            // when
            Order order = Order.builder()
                    .commonCodeId(mockId)
                    .member(mockMember)
                    .build();

            // then
            assertThat(order.getMember()).isSameAs(mockMember);
            assertThat(order.getCommonCodeId()).isSameAs(mockId);
            assertThat(order.getCommonCodeId().getGroupId()).isEqualTo(orderGroupCode);
        }
    }

    @Nested
    class changeOrderState{

        @Test
        void 변경성공_주문_상태() {
            // given
            CommonCodeId oldCommonCodeId = new CommonCodeId(CodeName.주문대기);
            Member mockMember = mock(Member.class);
            Order order = Order.builder()
                    .commonCodeId(oldCommonCodeId)
                    .member(mockMember)
                    .build();

            CommonCodeId newCommonCodeId = new CommonCodeId(CodeName.결제완료);
            // when
            order.changeOrderState(newCommonCodeId);

            // then
            assertThat(order.getCommonCodeId()).isEqualTo(newCommonCodeId);
        }
    }


    @Nested
    class setter {

        @Test
        void 설정성공_결제키(){
            // given
            CommonCodeId mockId = new CommonCodeId(CodeName.주문대기);
            Member mockMember = mock(Member.class);

            Order order = Order.builder()
                    .commonCodeId(mockId)
                    .member(mockMember)
                    .build();

            String payAuthKey = "test-payment-key";

            // when
            order.setPayAuthKey(payAuthKey);

            // then
            assertThat(order.getPayAuthKey()).isEqualTo(payAuthKey);
        }

        @Test
        void 설정성공_주문번호(){
            // given
            CommonCodeId mockId = new CommonCodeId(CodeName.주문대기);
            Member mockMember = mock(Member.class);

            Order order = Order.builder()
                    .commonCodeId(mockId)
                    .member(mockMember)
                    .build();

            String orderSerial = "orderSerial";

            // when
            order.setOrderSerial(orderSerial);

            // then
            assertThat(order.getOrderSerial()).isEqualTo(orderSerial);
        }
    }
}
