package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
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
public class OrderItemOptionRepositoryTest {

    @Autowired
    private OrderItemOptionRepository orderItemOptionRepository;

    @PersistenceContext
    private EntityManager em;

    private Order order;
    private ItemOption itemOption;

    @BeforeEach
    void init() {
        order = em.find(Order.class, 1L);
        itemOption = em.find(ItemOption.class, 1L);
    }

    @Nested
    class create {

        @Test
        void 성공_주문_아이템_저장() {
            // given
            OrderItemOption orderItemOption = OrderItemOption.builder()
                    .itemOption(itemOption)
                    .order(order)
                    .salesPrice(1000)
                    .count(1)
                    .build();

            // when
            OrderItemOption savedOrderItemOption = orderItemOptionRepository.save(orderItemOption);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedOrderItemOption).isNotNull(),
                    () -> assertThat(savedOrderItemOption.getId()).isEqualTo(orderItemOption.getId()),
                    () -> assertThat(savedOrderItemOption.getItemOption()).isEqualTo(orderItemOption.getItemOption()),
                    () -> assertThat(savedOrderItemOption.getOrder()).isEqualTo(orderItemOption.getOrder()),
                    () -> assertThat(savedOrderItemOption.getSalesPrice()).isEqualTo(orderItemOption.getSalesPrice()),
                    () -> assertThat(savedOrderItemOption.getCount()).isEqualTo(orderItemOption.getCount())
            );
        }
    }

}
