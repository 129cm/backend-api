package com.d129cm.backendapi.cart.repository;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(JpaAuditingConfig.class)
public class ItemCartRepositoryTest {

    @Autowired
    private ItemCartRepository itemCartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Cart cart;
    private Item item;
    private Item item2;
    private ItemOption itemOption;
    private ItemOption itemOption2;

    @BeforeEach
    void setup() {
        cart = entityManager.find(Cart.class, 1L);
        item = entityManager.find(Item.class, 1L);
        item2 = entityManager.find(Item.class, 2L);
        itemOption = entityManager.find(ItemOption.class, 1L);
        itemOption2 = entityManager.find(ItemOption.class, 2L);
    }


    @Nested
    class Create {

        @Test
        @Sql("/test-item-cart.sql")
        void 성공_ItemCart_저장() {
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

    @Nested
    class findAllByCartId {

        @Test
        @Sql("/test-item-cart.sql")
        void 성공_cartId로_모든ItemCart조회() {
            ItemCart itemCart1 = ItemCart.builder()
                    .count(1)
                    .item(item)
                    .itemOption(itemOption)
                    .cart(cart)
                    .build();

            ItemCart itemCart2 = ItemCart.builder()
                    .count(2)
                    .item(item)
                    .itemOption(itemOption2)
                    .cart(cart)
                    .build();

            itemCartRepository.save(itemCart1);
            itemCartRepository.save(itemCart2);

            // when
            List<ItemCart> itemCarts = itemCartRepository.findAllByCartId(cart.getId());

            // then
            assertThat(itemCarts).hasSize(2);
            assertThat(itemCarts).containsExactlyInAnyOrder(itemCart1, itemCart2);
        }
    }

    @Nested
    class findByItemIdAndItemOptionIdAndCartId {

        @Test
        @Sql("/test-item-cart.sql")
        void 성공_itemId_itemOptionId_cartId로_ItemCart조회() {
            // given
            ItemCart itemCart = ItemCart.builder()
                    .count(1)
                    .item(item)
                    .itemOption(itemOption)
                    .cart(cart)
                    .build();

            itemCartRepository.save(itemCart);

            ItemCart itemCart2 = ItemCart.builder()
                    .count(2)
                    .item(item2)
                    .itemOption(itemOption2)
                    .cart(cart)
                    .build();

            itemCartRepository.save(itemCart2);

            // when
            Optional<ItemCart> itemCartOptional = itemCartRepository.findByItemIdAndItemOptionIdAndCartId(
                    item.getId(), itemOption.getId(), cart.getId());

            // then
            assertThat(itemCartOptional).isPresent();
            ItemCart foundItemCart = itemCartOptional.get();
            assertThat(foundItemCart.getId()).isEqualTo(itemCart.getId());
            assertThat(foundItemCart.getCount()).isEqualTo(itemCart.getCount());
            assertThat(foundItemCart.getItem()).isEqualTo(itemCart.getItem());
            assertThat(foundItemCart.getItemOption()).isEqualTo(itemCart.getItemOption());
            assertThat(foundItemCart.getCart()).isEqualTo(itemCart.getCart());
        }

        @Test
        @Sql("/test-item-cart.sql")
        void 실패_존재하지않는_조합으로_ItemCart조회() {
            // given
            Long nonExistentItemId = 999L;
            Long nonExistentItemOptionId = 999L;
            Long nonExistentCartId = 999L;

            // when
            Optional<ItemCart> itemCartOptional = itemCartRepository.findByItemIdAndItemOptionIdAndCartId(
                    nonExistentItemId, nonExistentItemOptionId, nonExistentCartId);

            // then
            assertThat(itemCartOptional).isEmpty();
        }
    }
}

