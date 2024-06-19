package com.d129cm.backendapi.Item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
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

    @Nested
    class update {
        @Test
        void 성공_아이템_옵션_추가() {
            // given
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .image("image").build();
            ItemOption mockOption = mock(ItemOption.class);

            // when
            item.addItemOption(mockOption);

            // then
            assertThat(item.getItemOptions()).hasSize(1);
            assertThat(item.getItemOptions()).contains(mockOption);
        }

        @Test
        void 실패_아이템_옵션_추가_null값() {
            // given
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .image("image").build();

            // when & then
            assertThatThrownBy(() -> item.addItemOption(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("itemOption은 null일 수 없습니다.");
        }

        @Test
        void 성공_브랜드_추가() {
            // given
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .image("image").build();
            Brand brand = mock(Brand.class);

            // when
            item.updateBrand(brand);

            // then
            assertThat(item.getBrand()).isEqualTo(brand);
        }

        @Test
        void 실패_브랜드_Null_추가() {
            // given
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .image("image").build();

            // when & then
            assertThatThrownBy(() -> item.updateBrand(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("brand는 null일 수 없습니다.");
        }
    }
}