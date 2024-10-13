package com.d129cm.backendapi.partners.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.Password;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PartnersTest {

    @Nested
    class create {
        @Test
        void 성공() {
            // given
            Brand brand = mock(Brand.class);
            String email = "test@naver.com";
            Password password = mock(Password.class);
            String businessNumber = "123-45-67890";

            // when
            Partners partners = Partners.builder()
                    .email(email)
                    .password(password)
                    .businessNumber(businessNumber)
                    .build();

            partners.setBrand(brand);

            // then
            Assertions.assertAll(
                    () -> assertThat(partners.getId()).isNull(),
                    () -> assertThat(partners.getEmail()).isEqualTo(email),
                    () -> assertThat(partners.getPassword()).isEqualTo(password),
                    () -> assertThat(partners.getBusinessNumber()).isEqualTo(businessNumber),
                    () -> assertThat(partners.getBrand()).isEqualTo(brand)
            );
        }

        @Test
        void 예외발생_필드가_Null일_때() {
            // given
            String email = "test@naver.com";
            Password password = mock(Password.class);
            String businessNumber = "123-45-67890";

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, password, businessNumber),
                    () -> assertThrowsNullPointerException(email, null, businessNumber),
                    () -> assertThrowsNullPointerException(email, password, null)
            );
        }

        private void assertThrowsNullPointerException(String email, Password password, String businessNumber) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Partners.builder()
                    .email(email)
                    .password(password)
                    .businessNumber(businessNumber)
                    .build());
        }
    }
}