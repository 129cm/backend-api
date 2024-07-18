package com.d129cm.backendapi.cart.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.cart.domain.ItemCart;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(JpaAuditingConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemCartRepositoryTest {

    @Autowired
    private ItemCartRepository itemCartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Cart cart;
    private Item item;
    private ItemOption itemOption;

    @BeforeEach
    void setup() {
        cart = entityManager.find(Cart.class, 1L);
        item = entityManager.find(Item.class, 1L);
        itemOption = entityManager.find(ItemOption.class, 1L);
    }

    @Nested
    class Create {

        @Test
        @Sql("/test-item-cart.sql")
        void 성공_ItemCart_저장() {
            // given

            ItemCart itemCart = ItemCart.builder()
                    .count(1)
                    .item(item)
                    .itemOption(itemOption)
                    .cart(cart)
                    .build();

            // when
            ItemCart savedItemCart = itemCartRepository.save(itemCart);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedItemCart).isNotNull(),
                    () -> assertThat(savedItemCart.getId()).isNotNull(),
                    () -> assertThat(savedItemCart.getCount()).isEqualTo(itemCart.getCount()),
                    () -> assertThat(savedItemCart.getItem()).isEqualTo(itemCart.getItem()),
                    () -> assertThat(savedItemCart.getItemOption()).isEqualTo(itemCart.getItemOption()),
                    () -> assertThat(savedItemCart.getCart()).isEqualTo(itemCart.getCart())
            );
        }
    }
}