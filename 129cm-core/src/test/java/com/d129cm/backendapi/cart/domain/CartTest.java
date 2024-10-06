package com.d129cm.backendapi.cart.domain;

import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {

    @Nested
    class Create {

        @Test
        void 생성성공_주어진_필드로_장바구니_생성() {
            // given
            Member member = Mockito.mock(Member.class);

            // when
            Cart cart = new Cart();
            cart.setMember(member);

            // then
            Assertions.assertAll(
                    () -> assertThat(cart.getId()).isNull(),
                    () -> assertThat(cart.getMember()).isEqualTo(member)
            );
        }
    }
}
