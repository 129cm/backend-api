package com.d129cm.backendapi.brand.domain;

import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
            Partners partners = Mockito.mock(Partners.class);

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
}