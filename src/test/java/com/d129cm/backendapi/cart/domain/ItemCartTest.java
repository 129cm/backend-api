package com.d129cm.backendapi.cart.domain;

import com.d129cm.backendapi.fixture.Fixture;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ItemCartTest {

    private Fixture fixture = new Fixture();

    @Nested
    class Create {

        @Test
        void 생성성공_주어진_필드로_ItemCart_생성() {
            // given
            Integer count = 1;
            Item item = mock(Item.class);
            ItemOption itemOption = mock(ItemOption.class);
            Cart cart = mock(Cart.class);

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
            Item item = mock(Item.class);
            ItemOption itemOption = mock(ItemOption.class);
            Cart cart = mock(Cart.class);

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

    @Nested
    class increaseCount {

        @Test
        void count증가_이미_존재하는_제품_장바구니추가() {
            // given
            int count = 1;
            ItemCart itemCart = ItemCart.builder()
                    .count(1)
                    .item(mock(Item.class))
                    .itemOption(mock(ItemOption.class))
                    .cart(mock(Cart.class))
                    .build();
            int additionalCount = 2;

            // when
            itemCart.increaseCount(additionalCount);

            // then
            assertThat(itemCart.getCount()).isEqualTo(count + additionalCount);
        }
    }

    @Nested
    class updateCount {

        @Test
        void count수정_이미_존재하는_제품_수량_수정() {
            // given
            int count = 5;
            ItemCart itemCart = ItemCart.builder()
                    .count(1)
                    .item(mock(Item.class))
                    .itemOption(mock(ItemOption.class))
                    .cart(mock(Cart.class))
                    .build();
            int updateCount = 2;

            // when
            itemCart.updateCount(updateCount);

            // then
            assertThat(itemCart.getCount()).isEqualTo(updateCount);
        }
    }
}
