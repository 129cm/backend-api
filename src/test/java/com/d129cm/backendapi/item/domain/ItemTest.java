package com.d129cm.backendapi.item.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_아이템_생성() {
            // given
            String name = "상품 이름";
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";

            // when
            Item item = Item.builder()
                    .name(name)
                    .price(price)
                    .image(image)
                    .description(description)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(item.getId()).isNull(),
                    () -> assertThat(item.getName()).isEqualTo(name),
                    () -> assertThat(item.getPrice()).isEqualTo(price),
                    () -> assertThat(item.getImage()).isEqualTo(image),
                    () -> assertThat(item.getDescription()).isEqualTo(description)
            );
        }

        @Test
        void 예외발생_아이템_필드가_Null일_때() {
            // given
            String name = "상품 이름";
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, price, image, description),
                    () -> assertThrowsNullPointerException(name, null, image, description),
                    () -> assertThrowsNullPointerException(name, price, null, description),
                    () -> assertThrowsNullPointerException(name, price, image, null)

            );
        }

        @Test
        void 예외발생_아이템_가격이_0보다_작을_때() {
            // given
            String name = "상품 이름";
            Integer wrongPrice = -1000;
            String image = "상품 이미지";
            String description = "상품 설명";

            // when & then
            Assertions.assertThrows(IllegalArgumentException.class, () -> Item.builder()
                    .name(name)
                    .price(wrongPrice)
                    .image(image)
                    .description(description)
                    .build());
        }

        private void assertThrowsNullPointerException(String name, Integer price, String image, String description) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Item.builder()
                    .name(name)
                    .price(price)
                    .image(image)
                    .description(description)
                    .build());
        }
    }
}
