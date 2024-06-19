package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.item.domain.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Nested
    class create {
        @Test
        void 성공_아이템_저장() {
            //given
            Item item = Item.builder()
                    .name("상품 이름")
                    .price(1000)
                    .image("상품 이미지")
                    .description("상품 설명")
                    .build();

            // when
            Item savedItem = itemRepository.save(item);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedItem).isNotNull(),
                    () -> assertThat(savedItem.getId()).isEqualTo(item.getId()),
                    () -> assertThat(savedItem.getName()).isEqualTo(item.getName()),
                    () -> assertThat(savedItem.getPrice()).isEqualTo(item.getPrice()),
                    () -> assertThat(savedItem.getImage()).isEqualTo(item.getImage()),
                    () -> assertThat(savedItem.getDescription()).isEqualTo(item.getDescription())
            );
        }
    }
}
