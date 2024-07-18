package com.d129cm.backendapi.member.domain;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberTest {

    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_멤버_생성() {
            // given
            String email = "test@naver.com";
            Password password = Mockito.mock(Password.class);
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
            Password password = Mockito.mock(Password.class);
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

        private void assertThrowsNullPointerException(String email, Password password, String name, Address address) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Member.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .address(address)
                    .build());
        }
    }

    @Nested
    class SetCart {

        @Test
        void 연관관계생성_멤버_장바구니() {
            // given
            Member member = Member.builder()
                    .email("test@naver.com")
                    .password(Mockito.mock(Password.class))
                    .name("이름")
                    .address(Mockito.mock(Address.class))
                    .build();

            Cart cart = Mockito.mock(Cart.class);

            // when
            member.setCart(cart);

            // then
            assertThat(member.getCart()).isEqualTo(cart);
            verify(cart).setMember(member);
        }
    }
}
