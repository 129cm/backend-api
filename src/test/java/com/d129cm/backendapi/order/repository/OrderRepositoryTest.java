package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.common.domain.CommonCode;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
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
@Sql({"/clean-up.sql", "/test-item-cart.sql"})
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
    class create {

        @Test
        void 성공_주문_저장() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);

            Order order = Order.builder()
                    .commonCodeId(commonCodeId)
                    .member(member)
                    .build();

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
