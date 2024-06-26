package com.d129cm.backendapi.Item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
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
            List<ItemOption> options = Collections.singletonList(mock(ItemOption.class));

            // when
            Item item = Item.builder()
                    .name(name)
                    .price(price)
                    .image(image)
                    .description(description)
                    .itemOptions(options)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(item.getId()).isNull(),
                    () -> assertThat(item.getName()).isEqualTo(name),
                    () -> assertThat(item.getPrice()).isEqualTo(price),
                    () -> assertThat(item.getImage()).isEqualTo(image),
                    () -> assertThat(item.getDescription()).isEqualTo(description),
                    () -> assertThat(item.getItemOptions()).hasSize(1)
            );
        }

        @Test
        void 예외발생_아이템_필드가_Null일_때() {
            // given
            String name = "상품 이름";
            Integer price = 1000;
            String image = "상품 이미지";
            String description = "상품 설명";
            ItemOption mockOption = mock(ItemOption.class);
            List<ItemOption> options = new ArrayList<>();
            options.add(mockOption);

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, price, image, description, options),
                    () -> assertThrowsNullPointerException(name, null, image, description, options),
                    () -> assertThrowsNullPointerException(name, price, null, description, options),
                    () -> assertThrowsNullPointerException(name, price, image, null, options),
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
        void 성공_아이템_옵션_추가() {
            // given
            List<ItemOption> options = new ArrayList<>();
            options.add(mock(ItemOption.class));
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .itemOptions(options)
                    .image("image").build();
            ItemOption mockOption = mock(ItemOption.class);

            // when
            item.addItemOption(mockOption);

            // then
            assertThat(item.getItemOptions()).hasSize(2);
            assertThat(item.getItemOptions()).contains(mockOption);
        }

        @Test
        void 실패_아이템_옵션_추가_null값() {
            // given
            String itemName = "testItem";
            Integer price = 10000;
            String desc = "아이템 설명";
            String image = "이미지";

            // when & then
            assertThatThrownBy(() -> Item.builder()
                    .name(itemName)
                    .price(price)
                    .description(desc)
                    .image(image)
                    .itemOptions(null).build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아이템 옵션은 null일 수 없습니다.");
        }

        @Test
        void 실패_아이템_옵션_추가_empty() {
            // given
            String itemName = "testItem";
            Integer price = 10000;
            String desc = "아이템 설명";
            String image = "이미지";
            List<ItemOption> options = new ArrayList<>();

            // when & then
            assertThatThrownBy(() -> Item.builder()
                    .name(itemName)
                    .price(price)
                    .description(desc)
                    .image(image)
                    .itemOptions(options).build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아이템 옵션은 null일 수 없습니다.");

        }

        @Test
        void 성공_브랜드_추가() {
            // given
            List<ItemOption> options = Collections.singletonList(mock(ItemOption.class));
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .image("image")
                    .itemOptions(options)
                    .build();
            Brand brand = mock(Brand.class);

            // when
            item.updateBrand(brand);

            // then
            assertThat(item.getBrand()).isEqualTo(brand);
        }

        @Test
        void 실패_브랜드_Null_추가() {
            // given
            List<ItemOption> options = Collections.singletonList(mock(ItemOption.class));
            Item item = Item.builder()
                    .name("testItem")
                    .price(1000)
                    .description("desc")
                    .itemOptions(options)
                    .image("image").build();

            // when & then
            assertThatThrownBy(() -> item.updateBrand(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("brand는 null일 수 없습니다.");
        }
    }
}