package com.d129cm.backendapi.order.service;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
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
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderManager orderManager;

    @Mock
    private OrderItemOptionManager orderItemOptionManager;

    @Nested
    class createOrder {

        @Test
        void 주문번호반환_order_생성() {
            // given
            Long brandId = 1L;
            String brandName = "브랜드 이름";

            List<ItemWithOptionForOrderResponse> itemResponse = new ArrayList<>();
            itemResponse.add(new ItemWithOptionForOrderResponse(1L, "아이템 이름", 1000, "아이템 이미지", 1L, "아이템 옵션 이름", 100, 1));
            List<BrandsForOrderResponse> brandsForOrderResponses = new ArrayList<>();
            brandsForOrderResponses.add(new BrandsForOrderResponse(brandId, brandName, itemResponse));
            CreateOrderDto createOrderDto = new CreateOrderDto(brandsForOrderResponses);

            Member member = MemberFixture.createMember("abc@example.com");
            String orderSerial = "20240915-2345678";

            Order order = Order.builder()
                    .commonCodeId(new CommonCodeId(CodeName.주문대기))
                    .member(member)
                    .build();
            order.setOrderSerial(orderSerial);

            when(orderManager.createOrder(member)).thenReturn(order);
            doNothing().when(orderItemOptionManager).createOrderItemOption(order, createOrderDto);

            // when
            String result = orderService.createOrder(createOrderDto, member);

            // then
            assertAll(
                    () -> verify(orderManager).createOrder(member),
                    () -> verify(orderItemOptionManager).createOrderItemOption(order, createOrderDto),
                    () -> assertThat(result).isEqualTo(orderSerial),
                    () -> assertThat(order.getMember()).isEqualTo(member)
            );
        }
    }

}
