package com.d129cm.backendapi.partners.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class PartnersTest {

    @Nested
    class create {
        @Test
        void 성공() {
            // given
            Brand brand = Mockito.mock(Brand.class);
            String email = "test@naver.com";
            String password = "asdf1234!";
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
            String password = "asdf1234!";
            String businessNumber = "123-45-67890";

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, password, businessNumber),
                    () -> assertThrowsNullPointerException(email, null, businessNumber),
                    () -> assertThrowsNullPointerException(email, password, null)
            );
        }

        private void assertThrowsNullPointerException(String email, String password, String businessNumber) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Partners.builder()
                    .email(email)
                    .password(password)
                    .businessNumber(businessNumber)
                    .build());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"123-45-6789", "123-456-7890", "123-45-678901", "001-23-45678", "123-45-6789a"})
        void 예외발생_유효하지_않은_사업자번호(String inputNumber) {
            // given
            String email = "test@naver.com";
            String password = "asdf1234!";

            // when & then
            Assertions.assertThrows(IllegalArgumentException.class, () -> Partners.builder()
                    .email(email)
                    .password(password)
                    .businessNumber(inputNumber)
                    .build());
        }
    }
}
