package com.d129cm.backendapi.member.domain;

import com.d129cm.backendapi.common.domain.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_멤버_생성() {
            // given
            String email = "test@naver.com";
            String password = "Asdf1234!";
            String name = "이름";
            Address address = Mockito.mock(Address.class);

            // when
            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .address(address)
                    .build();

            // then
            Assertions.assertAll(
                    () -> assertThat(member.getId()).isNull(),
                    () -> assertThat(member.getEmail()).isEqualTo(email),
                    () -> assertThat(member.getPassword()).isEqualTo(password),
                    () -> assertThat(member.getName()).isEqualTo(name),
                    () -> assertThat(member.getAddress()).isEqualTo(address)
            );
        }

        @Test
        void 예외발생_멤버_필드가_Null일_때() {
            // given
            String email = "test@naver.com";
            String password = "Asdf1234!";
            String name = "이름";
            Address address = Mockito.mock(Address.class);

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, password, name, address),
                    () -> assertThrowsNullPointerException(email, null, name, address),
                    () -> assertThrowsNullPointerException(email, password, null, address),
                    () -> assertThrowsNullPointerException(email, password, name, null)
            );
        }

        private void assertThrowsNullPointerException(String email, String password, String name, Address address) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Member.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .address(address)
                    .build());
        }
    }
}
