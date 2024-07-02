package com.d129cm.backendapi.brand.domain;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class BrandTest {

    @Nested
    class create {
        @Test
        void 성공() {
            // given
            String name = "브랜드";
            String image = "이미지";
            String description = "설명";
            Partners partners = mock(Partners.class);

            // when
            Brand brand = Brand.builder()
                    .name(name)
                    .image(image)
                    .description(description)
                    .build();

            brand.setPartners(partners);

            // then
            Assertions.assertAll(
                    () -> Assertions.assertNull(brand.getId()),
                    () -> Assertions.assertEquals(brand.getName(), name),
                    () -> Assertions.assertEquals(brand.getImage(), image),
                    () -> Assertions.assertEquals(brand.getDescription(), description),
                    () -> Assertions.assertEquals(brand.getPartners(), partners)
            );
        }

        @Test
        void 예외발생_필드가_Null일_때() {
            // given
            String name = "브랜드";
            String image = "이미지";
            String description = "설명";

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, image, description),
                    () -> assertThrowsNullPointerException(name, null, description),
                    () -> assertThrowsNullPointerException(name, image, null)
            );
        }

        private void assertThrowsNullPointerException(String name, String image, String description) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Brand.builder()
                    .name(name)
                    .image(image)
                    .description(description)
                    .build());
        }
    }

    @Nested
    class update {

        @Test
        void 성공_아이템_추가() {
            // given
            Brand brand = Brand.builder()
                    .name("브랜드")
                    .image("이미지")
                    .description("설명")
                    .build();
            Item mockItem = mock(Item.class);

            // when
            brand.addItem(mockItem);

            // then
            assertThat(brand.getItems()).contains(mockItem);
            assertThat(brand.getItems()).hasSize(1);
        }

        @Test
        void 실패_아이템_null_추가() {
            // given
            Brand brand = Brand.builder()
                    .name("브랜드")
                    .image("이미지")
                    .description("설명")
                    .build();

            // when & then
            assertThatThrownBy(() -> brand.addItem(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("item은 null일 수 없습니다.");

        }
    }
}