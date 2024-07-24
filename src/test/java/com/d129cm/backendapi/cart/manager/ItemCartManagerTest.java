package com.d129cm.backendapi.cart.manager;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.cart.repository.ItemCartRepository;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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
}
