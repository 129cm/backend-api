package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemCartManagerTest {

    @InjectMocks
    private ItemCartManager itemCartManager;

    @Mock
    private ItemCartRepository itemCartRepository;

    @Nested
    class createItemCart {

        @Test
        void 성공_ItemCart_생성() {
            // given
            ItemCart itemCart = ItemCart.builder()
                    .count(1)
                    .item(mock(Item.class))
                    .itemOption(mock(ItemOption.class))
                    .cart(mock(Cart.class))
                    .build();

            // when
            itemCartManager.createItemCart(itemCart);

            // then
            verify(itemCartRepository, times(1)).save(itemCart);
        }
    }

    @Nested
    class getItemCart {

        @Test
        void 성공_ItemCart_조회() {
            // given
            Long cartId = 1L;
            List<ItemCart> itemCarts = new ArrayList<>();
            when(itemCartRepository.findAllByCartId(cartId)).thenReturn(itemCarts);

            // when
            itemCartManager.getItemCart(cartId);

            // then
            verify(itemCartRepository, times(1)).findAllByCartId(cartId);
        }
    }

    @Nested
    class findItemCart{
        @Test
        void ItemCart반환_주어진조건으로_itemcart조회() {
            // given
            CartItemRequest request = new CartItemRequest(1L, 2L, 10);
            Long cartId = 3L;
            Optional<ItemCart> mockItemCart = Optional.ofNullable(mock(ItemCart.class));

            when(itemCartRepository.findByItemIdAndItemOptionIdAndCartId(1L, 2L, 3L))
                    .thenReturn(mockItemCart);

            // when
            ItemCart result = itemCartManager.findItemCart(request, cartId);

            // then
            assertThat(result).isEqualTo(mockItemCart.get());
            verify(itemCartRepository).findByItemIdAndItemOptionIdAndCartId(1L, 2L, 3L);
        }
    }

    @Nested
    class increaseCount{
        @Test
        void count증가_후_업데이트_이미_존재하는_itemcart인경우() {
            // given
            ItemCart mockItemCart = mock(ItemCart.class);
            int additionalCount = 2;

            // when
            itemCartManager.increaseCount(mockItemCart, additionalCount);

            // then
            verify(mockItemCart).increaseCount(additionalCount);
            verify(itemCartRepository).save(mockItemCart);
        }
    }

    @Nested
    class updateItemQuantityInCart{
        @Test
        void 성공_카트아이템_수량조정() {
            // given
            Cart cart = Mockito.mock(Cart.class);
            ItemCart itemCart = Mockito.mock(ItemCart.class);
            when(itemCartRepository.findByItemIdAndItemOptionIdAndCartId(1L, 2L, 3L)).thenReturn(Optional.ofNullable(itemCart));
            when(cart.getId()).thenReturn(3L);
            CartItemUpdateRequest request = new CartItemUpdateRequest(1L, 2L, 5);

            // when
            itemCartManager.updateItemQuantityInCart(cart, request);

            // then
            verify(itemCartRepository).findByItemIdAndItemOptionIdAndCartId(request.itemId(), request.itemOptionId(), cart.getId());
            verify(itemCart).updateCount(request.count());
        }

        @Test
        void NotFoundException_존재하자얺는_itemcart를_수정하려는_경우() {
            // given
            Cart cart = Mockito.mock(Cart.class);
            when(cart.getId()).thenReturn(3L);
            CartItemUpdateRequest request = new CartItemUpdateRequest(1L, 2L, 5);

            when(itemCartRepository.findByItemIdAndItemOptionIdAndCartId(request.itemId(), request.itemOptionId(), cart.getId()))
                    .thenReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundException.class, () -> {
                itemCartManager.updateItemQuantityInCart(cart, request);
            });
        }
    }
}
