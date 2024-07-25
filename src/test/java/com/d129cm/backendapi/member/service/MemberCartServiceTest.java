package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.manager.ItemCartManager;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.fixture.Fixture;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartForMemberResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberCartServiceTest {

    @InjectMocks
    private MemberCartService memberCartService;

    @Mock
    private ItemManager itemManager;

    @Mock
    private ItemOptionManager itemOptionManager;

    @Mock
    private ItemCartManager itemCartManager;

    private Fixture fixture = new Fixture();

    @Nested
    class addItemToCart{
        @Test
        void 성공_아이템을_장바구니에_추가() {
            // given
            Member member = Mockito.mock(Member.class);
            Cart cart = Mockito.mock(Cart.class);
            when(member.getCart()).thenReturn(cart);

            Item item = Mockito.mock(Item.class);
            ItemOption itemOption = Mockito.mock(ItemOption.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, 3);

            when(itemManager.getItem(request.itemId())).thenReturn(item);
            when(itemOptionManager.getItemOption(request.itemOptionId())).thenReturn(itemOption);

            // when
            memberCartService.addItemToCart(member, request);

            // then
            verify(itemManager).getItem(request.itemId());
            verify(itemOptionManager).getItemOption(request.itemOptionId());
            verify(itemCartManager).createItemCart(any(ItemCart.class));
        }

        @Test
        void 예외반환_수량이_음수일_때() {
            // given
            Member member = Mockito.mock(Member.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, -1);

            // when & then
            BadRequestException e = BadRequestException.negativeQuantityLimit();
            Assertions.assertThatThrownBy(() -> memberCartService.addItemToCart(member, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }


        @Test
        void 예외반환_수량이_100을_초과할_때() {
            // given
            Member member = Mockito.mock(Member.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, 101);

            // when & then
            BadRequestException e = BadRequestException.exceedQuantityLimit(100);
            Assertions.assertThatThrownBy(() -> memberCartService.addItemToCart(member, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }

    @Nested
    class getCart {

        @Test
        void CartForMemberResponse리스트반환_카트_조회() {
            // given

            Member member = fixture.createMember();
            Cart cart = fixture.createCart(member);
            Brand brand = fixture.createBrand();
            Item item = fixture.createItem(brand);
            ItemOption itemOption = fixture.createItemOption(item);
            ItemCart itemCart1 = fixture.createItemCart(item, itemOption);
            List<ItemCart> itemCarts = new ArrayList<>();
            itemCarts.add(itemCart1);

            when(itemCartManager.getItemCart(cart.getId())).thenReturn(itemCarts);

            List<CartForMemberResponse> expectedResponses = itemCarts.stream()
                    .map(CartForMemberResponse::of)
                    .collect(Collectors.toList());

            // when
            List<CartForMemberResponse> responses = memberCartService.getCart(member);

            // then
            verify(itemCartManager).getItemCart(cart.getId());
            assertThat(responses).isEqualTo(expectedResponses);
        }
    }
}
