package com.d129cm.backendapi.item.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class ItemOptionTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_아이템_옵션_생성() {
            // given
            String name = "옵션 이름";
            Integer quantity = 100;
            Integer price = 10000;

            // when
            ItemOption itemOption = ItemOption.builder()
                    .name(name)
                    .quantity(quantity)
                    .optionPrice(price)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(itemOption.getId()).isNull(),
                    () -> assertThat(itemOption.getName()).isEqualTo(name),
                    () -> assertThat(itemOption.getQuantity()).isEqualTo(quantity)
            );
        }

        @Test
        void 예외발생_아이템_옵션_필드가_Null일_때() {
            // given
            String name = "옵션 이름";
            Integer quantity = 100;
            Integer price = 10000;

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsIllegalArgumentException(null, quantity, price, "이름은 null일 수 없습니다."),
                    () -> assertThrowsIllegalArgumentException(name, null, price, "수량은 null일 수 없습니다."),
                    () -> assertThrowsIllegalArgumentException(name, quantity, null, "옵션 가격은 null일 수 없습니다.")
            );
        }

        private void assertThrowsIllegalArgumentException(String name, Integer quantity, Integer price, String message) {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> ItemOption.builder()
                    .name(name)
                    .quantity(quantity)
                    .optionPrice(price)
                    .build());
            assertThat(message).isEqualTo(exception.getMessage());
        }
    }

    @Nested
    class update {

        @Test
        void 성공_Item_추가() {
            // given
            ItemOption itemOption = ItemOption.builder()
                    .name("옵션명")
                    .quantity(10)
                    .optionPrice(1000)
                    .build();
            Item item = mock(Item.class);

            // when
            itemOption.updateItem(item);

            // then
            assertThat(itemOption.getItem()).isEqualTo(item);
        }

        @Test
        void 실패_Item_null_추가() {
            // given
            ItemOption itemOption = ItemOption.builder()
                    .name("옵션명")
                    .quantity(10)
                    .optionPrice(1000)
                    .build();

            // when
            assertThatThrownBy(() -> itemOption.updateItem(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("item은 null일 수 없습니다.");

        }
    }
}