package com.d129cm.backendapi.cart.repository;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.fixture.AddressFixture;
import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(JpaAuditingConfig.class)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Nested
    class create {

        @Test
        void 성공_장바구니_저장() {
            // given
            Member member = Member.builder()
                    .email("test@naver.com")
                    .password(Mockito.mock(Password.class))
                    .name("이름")
                    .address(Mockito.mock(Address.class))
                    .build();
            Cart cart = new Cart();
            member.setCart(cart);

            // when
            Cart savedCart = cartRepository.save(cart);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedCart).isNotNull(),
                    () -> assertThat(savedCart.getId()).isEqualTo(cart.getId()),
                    () -> assertThat(savedCart.getMember()).isEqualTo(member)

            );
        }
    }
}
