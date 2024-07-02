package com.d129cm.backendapi.item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
            List<ItemOption> itemOptions = new ArrayList<>();
            itemOptions.add(mock(ItemOption.class));

            // when
            Item item = Item.builder()
                    .name(name)
                    .price(price)
                    .image(image)
                    .description(description)
                    .itemOptions(itemOptions)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(item.getId()).isNull(),
                    () -> assertThat(item.getName()).isEqualTo(name),
                    () -> assertThat(item.getPrice()).isEqualTo(price),
                    () -> assertThat(item.getImage()).isEqualTo(image),
                    () -> assertThat(item.getDescription()).isEqualTo(description),
                    () -> assertThat(item.getItemOptions()).isEqualTo(itemOptions)
            );
        }

        @Test
        void 예외발생_아이템_필드가_Null일_때() {
            // given
            String name = "상품 이름";
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";
            ItemOption itemOption = mock(ItemOption.class);
            List<ItemOption> itemOptions = List.of(itemOption);

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, price, image, description, itemOptions),
                    () -> assertThrowsNullPointerException(name, null, image, description, itemOptions),
                    () -> assertThrowsNullPointerException(name, price, null, description, itemOptions),
                    () -> assertThrowsNullPointerException(name, price, image, null, itemOptions),
                    () -> assertThrowsNullPointerException(name, price, image, description, null)
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

        private void assertThrowsNullPointerException(String name, Integer price, String image, String description, List<ItemOption> options) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Item.builder()
                    .name(name)
                    .price(price)
                    .image(image)
                    .description(description)
                    .itemOptions(options)
                    .build());
        }
    }

    @Nested
    class update {

        @Test
        void 성공_아이템_브랜드_업데이트() {
            // given
            Item item = Item.builder()
                    .name("아이템이름")
                    .image("이미지")
                    .description("아이템설명")
                    .itemOptions(List.of(mock(ItemOption.class)))
                    .price(10000).build();
            Brand brand = mock(Brand.class);

            // when
            item.updateBrand(brand);

            // then
            assertThat(item.getBrand()).isEqualTo(brand);
        }

        @Test
        void 성공_아이템_옵션_추가() {
            // given
            List<ItemOption> options = new ArrayList<>();
            ItemOption option = mock(ItemOption.class);
            options.add(option);
            Item item = Item.builder()
                    .name("아이템이름")
                    .image("이미지")
                    .description("아이템설명")
                    .itemOptions(options)
                    .price(10000).build();

            // when
            item.addItemOption(option);

            // then
            assertThat(item.getItemOptions()).hasSize(2);
        }
    }
}
