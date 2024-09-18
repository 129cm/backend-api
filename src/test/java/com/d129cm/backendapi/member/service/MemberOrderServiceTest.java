package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.fixture.BrandFixture;
import com.d129cm.backendapi.fixture.ItemFixture;
import com.d129cm.backendapi.fixture.ItemOptionFixture;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;
import com.d129cm.backendapi.member.dto.ItemWithOptionForOrderResponse;
import com.d129cm.backendapi.member.dto.OrderFormForMemberResponse;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.*;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberOrderServiceTest {

    @InjectMocks
    private MemberOrderService memberOrderService;

    @Mock
    private ItemManager itemManager;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Mock
    private OrderManager orderManager;

    @Mock
    private OrderItemOptionManager orderItemOptionManager;

    private Member member;
    private Item item;
    private ItemOption itemOption;

    private static final int VALID_QUANTITY = 3;
    private static final int INVALID_QUANTITY_NEGATIVE = -1;
    private static final int INVALID_QUANTITY_EXCEEDS_LIMIT = 101;
    private static final int MAX_QUANTITY_FOR_ORDER = 100;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember("member@email.com");
        item = ItemFixture.createItem(BrandFixture.createBrand(mock(Partners.class)));
        itemOption = ItemOptionFixture.createItemOption(item);

        when(itemManager.getItem(1L)).thenReturn(item);
        when(itemOptionManager.getItemOption(2L)).thenReturn(itemOption);
    }


    @Nested
    class getOrderForm {

        @Test
        void 성공_아이템을_장바구니에_추가() {
            // given
            List<OrderFormDto> request = List.of(new OrderFormDto(1L, 2L, VALID_QUANTITY));

            // when
            OrderFormForMemberResponse response = memberOrderService.getOrderForm(request, member);

            // then
            assertNotNull(response);
            assertEquals("이름", response.userName());
            assertNotNull(response.address());
            assertEquals(1, response.brandsForOrderResponse().size());

            assertEquals("브랜드 이름", response.brandsForOrderResponse().get(0).brandName());
            assertEquals(1, response.brandsForOrderResponse().get(0).itemResponse().size());
            assertEquals("상품 이름", response.brandsForOrderResponse().get(0).itemResponse().get(0).itemName());
            assertEquals("상품 옵션 이름", response.brandsForOrderResponse().get(0).itemResponse().get(0).itemOptionName());
            assertEquals(VALID_QUANTITY, response.brandsForOrderResponse().get(0).itemResponse().get(0).count());
        }


        @Test
        void 예외발생_수량이_음수일_경우() {
            // given
            List<OrderFormDto> request = List.of(new OrderFormDto(1L, 2L, INVALID_QUANTITY_NEGATIVE));

            // when & then
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                memberOrderService.getOrderForm(request, member);
            });

            assertEquals("수량은 0 이하일 수 없습니다. 최소 1 이상의 값을 입력해 주세요.", exception.getMessage());
        }

        @Test
        void 예외발생_수량이_100_초과일_경우() {
            // given
            List<OrderFormDto> request = List.of(new OrderFormDto(1L, 2L, INVALID_QUANTITY_EXCEEDS_LIMIT ));

            // when & then
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                memberOrderService.getOrderForm(request, member);
            });

            assertEquals("적용할 수 있는 수량 " +MAX_QUANTITY_FOR_ORDER+ "(을)를 초과하였습니다", exception.getMessage());
        }
    }

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
            String result = memberOrderService.createOrder(createOrderDto, member);

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
