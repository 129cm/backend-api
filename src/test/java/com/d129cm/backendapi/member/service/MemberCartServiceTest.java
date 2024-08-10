package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.manager.ItemCartManager;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.fixture.*;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartForMemberResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import com.d129cm.backendapi.partners.domain.Partners;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private MemberFixture memberFixture = new MemberFixture();
    private ItemFixture itemFixture = new ItemFixture();
    private ItemOptionFixture itemOptionFixture = new ItemOptionFixture();
    private ItemCartFixture itemCartFixture = new ItemCartFixture();
    private PartnersFixture partnersFixture = new PartnersFixture();

    private static final int MAX_QUANTITY_FOR_CART = 100;

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
            when(itemCartManager.findItemCart(request, cart.getId())).thenReturn(null);

            // when
            memberCartService.addItemToCart(member, request);

            // then
            verify(itemManager).getItem(request.itemId());
            verify(itemOptionManager).getItemOption(request.itemOptionId());
            verify(itemCartManager).createItemCart(any(ItemCart.class));
        }

        @Test
        void 성공_이미존재하는_itemCart의_count증가() {
            // given
            ItemCart alreadyExistingItemCart = Mockito.mock(ItemCart.class);
            Member member = Mockito.mock(Member.class);
            Cart cart = Mockito.mock(Cart.class);
            when(member.getCart()).thenReturn(cart);

            Item item = Mockito.mock(Item.class);
            ItemOption itemOption = Mockito.mock(ItemOption.class);
            CartItemRequest request = new CartItemRequest(1L, 2L, 3);

            when(itemManager.getItem(request.itemId())).thenReturn(item);
            when(itemOptionManager.getItemOption(request.itemOptionId())).thenReturn(itemOption);
            when(itemCartManager.findItemCart(request, cart.getId())).thenReturn(alreadyExistingItemCart);

            // when
            memberCartService.addItemToCart(member, request);

            // then
            verify(itemManager).getItem(request.itemId());
            verify(itemOptionManager).getItemOption(request.itemOptionId());
            verify(itemCartManager).increaseCount(alreadyExistingItemCart, request.count());
        }
    }

    @Nested
    class getCart {

        @Test
        void CartForMemberResponse리스트반환_카트_조회() {
            // given

            Member member = memberFixture.createMember("user@example.com");
            Cart cart = CartFixture.createCart(member);
            Partners partners = partnersFixture.createPartners("partners@example.com", "123-12-12345");
            Brand brand = BrandFixture.createBrand(partners);
            Item item = itemFixture.createItem(brand);
            ItemOption itemOption = itemOptionFixture.createItemOption(item);
            ItemCart itemCart1 = itemCartFixture.createItemCart(item, itemOption);
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

    @Nested
    class updateItemQuantityInCart {

        @Test
        void CartForMemberResponse리스트반환_카트_조회() {
            // given
            Member member = memberFixture.createMember("user@example.com");
            Cart cart = CartFixture.createCart(member);
            CartItemUpdateRequest request = new CartItemUpdateRequest(1L, 2L, 3);

            // when
            memberCartService.updateItemQuantityInCart(member, request);

            // then
            verify(itemCartManager).updateItemQuantityInCart(cart, request);
        }
    }


    @Nested
    class deleteItemFromCart {

        @Test
        void 아이템카트_삭제() {
            // given
            Member member = memberFixture.createMember("user@example.com");
            Cart cart = CartFixture.createCart(member);
            Long itemId = 1L;
            Long itemOptionId = 2L;

            // when
            memberCartService.deleteItemFromCart(member, itemId, itemOptionId);

            // then
            verify(itemCartManager).deleteItemFromCart(cart, itemId, itemOptionId);
        }
    }

    @Nested
    class countValidation {

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
            CartItemRequest request = new CartItemRequest(1L, 2L, MAX_QUANTITY_FOR_CART + 1);

            // when & then
            BadRequestException e = BadRequestException.exceedQuantityLimit(100);
            Assertions.assertThatThrownBy(() -> memberCartService.addItemToCart(member, request))
                    .isInstanceOf(e.getClass()).hasMessage(e.getMessage());
        }
    }
}
