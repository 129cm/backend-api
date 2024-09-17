package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
@Sql(value = {"/clean-up.sql", "/test-common-code.sql", "/test-order.sql"})
@Import(JpaAuditingConfig.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    private Member member;

    @BeforeEach
    void init() {
        member = em.find(Member.class, 1L);
    }

    @Nested
    class searchOrders {

        @Test
        void OrdersSearchResponseDto반환_파라메터_없이_검색() {
            // given
            int size = 10;
            int page = 0;

            // when
            OrdersSearchResultDto responseDtos = orderRepository.searchOrders(null, null, null, null, size, page);

            // then
            assertThat(responseDtos.getOrders()).isNotNull();
            assertThat(responseDtos.getOrders().size()).isEqualTo(size);
        }
    }

    @Nested
    class findOrderDetailsByOrderId {

        @Test
        void OrderDetailsDto반환_주문_상세_조회() {
            // given
            Long orderId = 1L;

            // when
            OrderDetailsDto details = orderRepository.findOrderDetailsByOrderId(orderId);

            // then
            assertThat(details).isNotNull();
            assertThat(details.getOrderId()).isEqualTo(orderId);
            assertThat(details.getMemberId()).isEqualTo(member.getId());
        }
    }

    @Nested
    class create {

        @Test
        void 성공_주문_저장() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);
            String orderSerial = "orderSerial";
            Order order = Order.builder()
                    .commonCodeId(commonCodeId)
                    .member(member)
                    .build();
            order.setOrderSerial(orderSerial);

            // when
            Order savedOrder = orderRepository.save(order);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedOrder).isNotNull(),
                    () -> assertThat(savedOrder.getId()).isEqualTo(order.getId()),
                    () -> assertThat(savedOrder.getMember()).isEqualTo(order.getMember()),
                    () -> assertThat(savedOrder.getCommonCodeId()).isEqualTo(order.getCommonCodeId())
            );
        }
    }
}
