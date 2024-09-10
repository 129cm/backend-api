package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemOptionIdTest {

    @Nested
    class create {

        @Test
        void 생성성공_OrderItemOptionId_생성() {
            // given
            Long orderId = 10L;
            Long itemOptionId = 1L;

            // when
            OrderItemOptionId id = new OrderItemOptionId(orderId, itemOptionId);

            // then
            assertThat(id.getOrderId()).isEqualTo(orderId);
            assertThat(id.getItemOptionId()).isEqualTo(itemOptionId);
        }
    }

    @Nested
    class equalsAndHashCode {

        @Test
        void true_동일성_비교() {
            // given
            OrderItemOptionId id = new OrderItemOptionId(10L, 1L);

            // when & then
            assertThat(id.equals(id)).isTrue(); // 동일한 객체 비교
            assertThat(id.hashCode()).isEqualTo(id.hashCode());
        }

        @Test
        void true_동등성_비교() {
            // given
            OrderItemOptionId id1 = new OrderItemOptionId(10L, 1L);
            OrderItemOptionId id2 = new OrderItemOptionId(10L, 1L);

            // when & then
            assertThat(id1.equals(id2)).isTrue(); // 값이 동일한 객체 비교
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        void false_필드_값이_다른_객체_비교() {
            // given
            OrderItemOptionId id1 = new OrderItemOptionId(10L, 1L);
            OrderItemOptionId id2 = new OrderItemOptionId(20L, 2L);

            // when & then
            assertThat(id1.equals(id2)).isFalse(); // 값이 다른 객체 비교
            assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
        }

        @Test
        void false_Null과_비교() {
            // given
            OrderItemOptionId id = new OrderItemOptionId(10L, 1L);

            // when & then
            assertThat(id.equals(null)).isFalse(); // null과 비교
        }

        @Test
        void false_타입이_다른_객체와_비교() {
            // given
            OrderItemOptionId id = new OrderItemOptionId(10L, 1L);
            String differentType = "String";

            // when & then
            assertThat(id.equals(differentType)).isFalse(); // 다른 타입 객체와 비교
        }
    }
}