package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
            ItemCart mockItemCart = mock(ItemCart.class);

            when(itemCartRepository.findByItemIdAndItemOptionIdAndCartId(1L, 2L, 3L))
                    .thenReturn(mockItemCart);

            // when
            ItemCart result = itemCartManager.findItemCart(request, cartId);

            // then
            assertThat(result).isEqualTo(mockItemCart);
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
}
