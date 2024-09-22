package com.d129cm.backendapi.payment.service;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.fixture.*;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.payment.manager.PaymentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private ItemManager itemManager;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Mock
    private OrderManager orderManager;

    @Mock
    private OrderItemOptionManager orderItemOptionManager;

    @Mock
    private PaymentManager paymentManager;

    private Member member;
    private Item item;
    private ItemOption itemOption;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember("member@email.com");
        item = ItemFixture.createItem(BrandFixture.createBrand(mock(Partners.class)));
        itemOption = ItemOptionFixture.createItemOption(item);
    }

    @Nested
    class getTotalPrice {

        @Test
        void 총금액반환_주문한_아이템_가격을_모두_합한_총_금액_조회() {
            // given
            Long orderId = 1L;

            // when
            Integer result = paymentService.getTotalPrice(orderId);

            // then
            assertAll(
                    () -> verify(paymentManager).getTotalPrice(orderId)
            );
        }
    }

    @Nested
    class prepareOrder {

        @Test
        void 주문_준비() {
            // given
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), "20240916-2345678");
            String paymentKey = "paymentKey";
            Integer beforeQuantity1 = 100;
            Integer beforeQuantity2 = 100;

            ItemOption itemOption1 = ItemOptionFixture.createItemOption(mock(Item.class));
            ItemOption itemOption2 = ItemOptionFixture.createItemOption(mock(Item.class));

            Integer count1 = 3;
            Integer salePrice1 = 1000;
            Integer count2 = 2;
            Integer salePrice2 = 2000;

            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            OrderItemOption orderItemOption1 = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption1)
                    .count(count1)
                    .salesPrice(salePrice1)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .build();
            OrderItemOption orderItemOption2 = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption2)
                    .count(count2)
                    .salesPrice(salePrice2)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .build();
            orderItemOptions.add(orderItemOption1);
            orderItemOptions.add(orderItemOption2);

            when(orderItemOptionManager.getOrderItemOptionByOrderId(order.getId())).thenReturn(orderItemOptions);

            // when
            paymentService.prepareOrder(order, paymentKey);

            // then
            assertThat(paymentKey).isEqualTo(order.getPayAuthKey());
            assertThat(itemOption1.getQuantity()).isEqualTo(beforeQuantity1 - count1);
            assertThat(itemOption2.getQuantity()).isEqualTo(beforeQuantity2 - count2);
        }
    }

    @Nested
    class completeOrder {

        @Test
        void 주문상태변경_주문_완료() {
            // given
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), "20240916-2345678");
            Long orderId = 1L;
            CommonCodeId newCommonCodeId = new CommonCodeId(CodeName.결제완료);

            ItemOption itemOption = ItemOptionFixture.createItemOption(mock(Item.class));
            OrderItemOption orderItemOption = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption)
                    .count(1)
                    .salesPrice(1000)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .build();

            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            orderItemOptions.add(orderItemOption);

            when(orderItemOptionManager.getOrderItemOptionByOrderId(orderId)).thenReturn(orderItemOptions);

            // when
            paymentService.completeOrder(orderId);

            // then
            assertThat(orderItemOption.getCommonCodeId()).isEqualTo(newCommonCodeId);
        }
    }

    @Nested
    class undoOrder {

        @Test
        void 주문상태변경_주문_취소() {
            // given
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), "20240916-2345678");
            Long orderId = 1L;
            CommonCodeId newCommonCodeId = new CommonCodeId(CodeName.주문취소);

            ItemOption itemOption = ItemOptionFixture.createItemOption(mock(Item.class));
            OrderItemOption orderItemOption = OrderItemOption.builder()
                    .order(order)
                    .itemOption(itemOption)
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .count(1)
                    .salesPrice(1000)
                    .build();

            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            orderItemOptions.add(orderItemOption);

            when(orderItemOptionManager.getOrderItemOptionByOrderId(orderId)).thenReturn(orderItemOptions);

            // when
            paymentService.undoOrder(orderId);

            // then
            assertThat(orderItemOption.getCommonCodeId()).isEqualTo(newCommonCodeId);
        }
    }

    @Nested
    class getOrderByOrderSerial {

        @Test
        void Order반환_orderSerial로_order_조회() {
            // given
            String orderSerial = "20240916-2345678";
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), orderSerial);

            when(orderManager.getOrderByOrderSerial(orderSerial)).thenReturn(order);

            // when
            Order result = paymentService.getOrderByOrderSerial(orderSerial);

            // then
            verify(orderManager).getOrderByOrderSerial(orderSerial);
            assertThat(result).isEqualTo(order);
        }
    }
}
