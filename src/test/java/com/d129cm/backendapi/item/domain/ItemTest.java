package com.d129cm.backendapi.item.domain;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_아이템_생성() {
            // given
            String name = "상품 이름";
            Integer quantity = 100;
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";

            // when
            Item item = Item.builder()
                    .name(name)
                    .quantity(quantity)
                    .price(price)
                    .image(image)
                    .description(description)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(item.getId()).isNull(),
                    () -> assertThat(item.getName()).isEqualTo(name),
                    () -> assertThat(item.getQuantity()).isEqualTo(quantity),
                    () -> assertThat(item.getPrice()).isEqualTo(price),
                    () -> assertThat(item.getImage()).isEqualTo(image),
                    () -> assertThat(item.getDescription()).isEqualTo(description)
            );
        }

        @Test
        void 예외발생_멤버_필드가_Null일_때() {
            // given
            String name = "상품 이름";
            Integer quantity = 100;
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, quantity, price, image, description),
                    () -> assertThrowsNullPointerException(name, null, price, image, description),
                    () -> assertThrowsNullPointerException(name, quantity, null, image, description),
                    () -> assertThrowsNullPointerException(name, quantity, price, null, description),
                    () -> assertThrowsNullPointerException(name, quantity, price, image, null)

            );
        }

        private void assertThrowsNullPointerException(String name, Integer price, Integer quantity, String image, String description) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Item.builder()
                    .name(name)
                    .quantity(quantity)
                    .price(price)
                    .image(image)
                    .description(description)
                    .build());
        }
    }
}
