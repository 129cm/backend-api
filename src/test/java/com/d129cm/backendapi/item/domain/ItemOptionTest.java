package com.d129cm.backendapi.item.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemOptionTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_아이템_옵션_생성() {
            // given
            String name = "옵션 이름";
            Integer quantity = 100;

            // when
            ItemOption itemOption = ItemOption.builder()
                    .name(name)
                    .quantity(quantity)
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

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, quantity),
                    () -> assertThrowsNullPointerException(name, null)

            );
        }

        private void assertThrowsNullPointerException(String name, Integer quantity) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ItemOption.builder()
                    .name(name)
                    .quantity(quantity)
                    .build());
        }
    }
}
