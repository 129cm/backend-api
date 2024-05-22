package com.d129cm.backendapi.member.domain;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.partners.domain.Partners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MemberTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_멤버_생성() {
            // given
            String email = "test@naver.com";
            String password = "asdf1234!";
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
                    () -> Assertions.assertNull(member.getId()),
                    () -> Assertions.assertEquals(member.getEmail(), email),
                    () -> Assertions.assertEquals(member.getPassword(), password),
                    () -> Assertions.assertEquals(member.getName(), name),
                    () -> Assertions.assertEquals(member.getAddress(), address)
            );
        }

        @Test
        void 예외발생_멤버_필드가_Null일_때() {
            // given
            String email = "test@naver.com";
            String password = "asdf1234!";
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
