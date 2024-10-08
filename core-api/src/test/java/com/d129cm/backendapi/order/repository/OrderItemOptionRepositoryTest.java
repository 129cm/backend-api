package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
@Sql({"/clean-up.sql", "/test-common-code.sql", "/test-order-item-option.sql"})
@Import(JpaAuditingConfig.class)
public class OrderItemOptionRepositoryTest {

    @Autowired
    private OrderItemOptionRepository orderItemOptionRepository;

    @PersistenceContext
    private EntityManager em;

    @Nested
    class create {

        private Order order;
        private ItemOption itemOption;

        @BeforeEach
        void init() {
            order = em.find(Order.class, 1L);
            itemOption = em.find(ItemOption.class, 1L);
        }

        @Test
        void 성공_주문_아이템_저장() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);
            OrderItemOption orderItemOption = OrderItemOption.builder()
                    .itemOption(itemOption)
                    .order(order)
                    .salesPrice(1000)
                    .count(1)
                    .commonCodeId(commonCodeId)
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
                    () -> assertThat(savedOrderItemOption.getCount()).isEqualTo(orderItemOption.getCount()),
                    () -> assertThat(savedOrderItemOption.getCommonCodeId()).isEqualTo(commonCodeId)
            );
        }
    }

    @Nested
    class updateOrderItemOptionsCommonCodeId {

        private Order order;
        private ItemOption itemOption1;
        private ItemOption itemOption2;

        @BeforeEach
        void init() {
            order = em.find(Order.class, 1L);
            itemOption1 = em.find(ItemOption.class, 1L);
            itemOption2 = em.find(ItemOption.class, 2L);
        }

        @Test
        void 성공_orderItemOptionId_리스트를_이용하여_CodeId_를_020으로_변경() {
            // given
            OrderItemOptionId id1 = new OrderItemOptionId(order.getId(), itemOption1.getId());
            OrderItemOptionId id2 = new OrderItemOptionId(order.getId(), itemOption2.getId());
            String codeId = CodeName.주문완료.getCodeId();

            // when
            int updated = orderItemOptionRepository.updateOrderItemOptionsCommonCodeId(codeId, List.of(id1, id2));

            // then
            assertThat(updated).isEqualTo(2);
        }
    }

}
