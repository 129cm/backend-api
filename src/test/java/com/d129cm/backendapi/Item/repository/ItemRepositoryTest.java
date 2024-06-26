package com.d129cm.backendapi.Item.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.repository.ItemRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(JpaAuditingConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    class create {

        private Brand brand;

        @BeforeEach
        void setup() {
            brand = entityManager.find(Brand.class, 1L);
        }

        @Test
        @Sql("/test-item.sql")
        void 성공_아이템_저장() {
            //given
            List<ItemOption> itemOptions = new ArrayList<>();
            ItemOption mockOption = mock(ItemOption.class);
            itemOptions.add(mockOption);

            Item item = Item.builder()
                    .name("아이템")
                    .image("이미지")
                    .price(10000)
                    .description("설명")
                    .itemOptions(itemOptions)
                    .build();
            item.updateBrand(brand);

            // when
            Item savedItem = itemRepository.save(item);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedItem).isNotNull(),
                    () -> assertThat(savedItem.getId()).isEqualTo(item.getId()),
                    () -> assertThat(savedItem.getImage()).isEqualTo(item.getImage()),
                    () -> assertThat(savedItem.getPrice()).isEqualTo(item.getPrice()),
                    () -> assertThat(savedItem.getDescription()).isEqualTo(item.getDescription()),
                    () -> assertThat(savedItem.getItemOptions()).contains(mockOption)

            );
        }
    }
}
