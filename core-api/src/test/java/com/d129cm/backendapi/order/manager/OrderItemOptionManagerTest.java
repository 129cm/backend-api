package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.fixture.OrderFixture;
import com.d129cm.backendapi.fixture.OrderItemOptionFixture;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.domain.OrderItemOption;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.repository.OrderItemOptionRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemOptionManagerTest {

    @InjectMocks
    private OrderItemOptionManager orderItemOptionManager;

    @Mock
    private OrderItemOptionRepository orderItemOptionRepository;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Nested
    class getOrderItemOptionByOrderId {

        @Test
        void List_OrderItemOption_반환_orderI로_조회() {
            // given
            Long orderId = 1L;
            List<OrderItemOption> orderItemOptionList = new ArrayList<>();
            orderItemOptionList.add(mock(OrderItemOption.class));

            when(orderItemOptionRepository.findByOrderId(orderId)).thenReturn(orderItemOptionList);

            // when
            List<OrderItemOption> result = orderItemOptionManager.getOrderItemOptionByOrderId(orderId);

            // then
            verify(orderItemOptionRepository).findByOrderId(orderId);

        }
    }

    @Nested
    class createOrderItemOption {

        @Test
        void 저장성공_OrderItemOption() {
            // given
            Long brandId = 1L;
            String brandName = "브랜드 이름";
            Long itemId = 2L;
            Integer itemPrice = 1000;
            Long itemOptionId = 3L;
            Integer itemOptionPrice = 100;
            Integer count = 1;
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);

            List<ItemWithOptionForOrderResponse> itemResponse = new ArrayList<>();
            itemResponse.add(new ItemWithOptionForOrderResponse(itemId, "아이템 이름", itemPrice, "아이템 이미지", itemOptionId, "아이템 옵션 이름", itemOptionPrice, count));

            List<BrandsForOrderResponse> brandsForOrderResponses = new ArrayList<>();
            brandsForOrderResponses.add(new BrandsForOrderResponse(brandId, brandName, itemResponse));

            CreateOrderDto createOrderDto = new CreateOrderDto(brandsForOrderResponses);

            Member member = MemberFixture.createMember("abc@example.com");
            String orderSerial = "20240915-2345678";
            Order order = OrderFixture.makeOrderWithOrderSerial(member, orderSerial, 0);

            ItemOption itemOption = ItemOption.builder()
                    .id(itemOptionId)
                    .optionPrice(100)
                    .name("옵션 이름")
                    .quantity(100)
                    .build();

            when(itemOptionManager.getItemOption(itemOptionId)).thenReturn(itemOption);

            // when
            orderItemOptionManager.createOrderItemOption(order, createOrderDto);

            // then
            ArgumentCaptor<OrderItemOption> orderItemOptionCaptor = ArgumentCaptor.forClass(OrderItemOption.class);
            verify(orderItemOptionRepository, times(itemResponse.size())).save(orderItemOptionCaptor.capture());
            OrderItemOption capturedOrderItemOption = orderItemOptionCaptor.getValue();

            assertAll(
                    () -> assertThat(capturedOrderItemOption.getOrder()).isEqualTo(order),
                    () -> assertThat(capturedOrderItemOption.getItemOption()).isEqualTo(itemOption),
                    () -> assertThat(capturedOrderItemOption.getSalesPrice()).isEqualTo(itemPrice + itemOptionPrice),
                    () -> assertThat(capturedOrderItemOption.getCount()).isEqualTo(count),
                    () -> assertThat(capturedOrderItemOption.getCommonCodeId()).isEqualTo(commonCodeId)
            );
        }
    }

    @Nested
    class getOrderItemOptionId {

        @Test
        void OrderItemOption반환_OrderItemOptionId로_조회() {
            // given
            Long orderId = 1L;
            Long itemOptionId = 2L;
            OrderItemOptionId orderItemOptionId = new OrderItemOptionId(orderId, itemOptionId);

            OrderItemOption orderItemOption = OrderItemOptionFixture.makeOrderItemOption();

            when(orderItemOptionRepository.findById(orderItemOptionId)).thenReturn(Optional.ofNullable(orderItemOption));

            // when
            OrderItemOption result = orderItemOptionManager.getOrderItemOptionId(orderItemOptionId);

            // then
            assertThat(result).isNotNull();
            verify(orderItemOptionRepository).findById(orderItemOptionId);
            assertThat(result).isEqualTo(orderItemOption);
        }

        @Test
        void 에러반환_OrderItemOptionId로_조회() {
            // given
            Long orderId = 1L;
            Long itemOptionId = 2L;
            OrderItemOptionId orderItemOptionId = new OrderItemOptionId(orderId, itemOptionId);

            when(orderItemOptionRepository.findById(orderItemOptionId)).thenReturn(Optional.empty());

            // when & then
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> orderItemOptionManager.getOrderItemOptionId(orderItemOptionId));
        }
    }

    @Nested
    class modifyOrderState {

        @Test
        void 성공_orderItemOption_의_OrderState_변경() {
            // given
            OrderItemOptionId orderItemOptionId1 = new OrderItemOptionId(1L, 1L);
            OrderItemOptionId orderItemOptionId2 = new OrderItemOptionId(1L, 2L);
            List<OrderItemOptionId> ids = List.of(orderItemOptionId1, orderItemOptionId2);
            String codeId = CodeName.주문완료.getCodeId();

            when(orderItemOptionRepository.updateOrderItemOptionsCommonCodeId(codeId, ids)).thenReturn(2);

            // when
            int updated = orderItemOptionManager.modifyOrderState(codeId, ids);

            // then
            assertThat(updated).isEqualTo(2);
            verify(orderItemOptionRepository).updateOrderItemOptionsCommonCodeId(codeId, ids);
        }
    }
}
