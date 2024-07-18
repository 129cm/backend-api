package com.d129cm.backendapi.cart.domain;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemCartTest {

    @Nested
    class Create {

        @Test
        void 생성성공_주어진_필드로_ItemCart_생성() {
            // given
            Integer count = 1;
            Item item = Mockito.mock(Item.class);
            ItemOption itemOption = Mockito.mock(ItemOption.class);
            Cart cart = Mockito.mock(Cart.class);

            // when
            ItemCart itemCart = ItemCart.builder()
                    .count(count)
                    .item(item)
                    .itemOption(itemOption)
                    .cart(cart)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(itemCart.getCount()).isEqualTo(count),
                    () -> assertThat(itemCart.getItem()).isEqualTo(item),
                    () -> assertThat(itemCart.getItemOption()).isEqualTo(itemOption),
                    () -> assertThat(itemCart.getCart()).isEqualTo(cart)
            );
        }

        @Test
        void 예외발생_ItemCart_필드가_Null일_때() {
            // given
            Integer count = 1;
            Item item = Mockito.mock(Item.class);
            ItemOption itemOption = Mockito.mock(ItemOption.class);
            Cart cart = Mockito.mock(Cart.class);

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, item, itemOption, cart, "아이템 수량이 null일 수 없습니다."),
                    () -> assertThrowsNullPointerException(count, item, itemOption, null, "장바구니가 null일 수 없습니다.")
            );
        }

        private void assertThrowsNullPointerException(Integer count, Item item, ItemOption itemOption, Cart cart, String message) {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                    ItemCart.builder()
                            .count(count)
                            .item(item)
                            .itemOption(itemOption)
                            .cart(cart)
                            .build()
            );
            assertThat(message).isEqualTo(exception.getMessage());
        }
    }
}
