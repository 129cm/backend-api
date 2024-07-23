package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.manager.ItemCartManager;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.manager.ItemOptionManager;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
