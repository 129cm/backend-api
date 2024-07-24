package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.config.JpaAuditingConfig;
import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.domain.SortCondition;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(InitializeTestContainers.class)
@Import(JpaAuditingConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
            // given
            ItemOption mockOption = ItemOption.builder()
                    .name("아이템옵션")
                    .quantity(100)
                    .optionPrice(1000)
                    .build();

            Item item = Item.builder()
                    .name("아이템")
                    .image("이미지")
                    .price(10000)
                    .description("설명")
                    .build();
            item.addItemOption(mockOption);
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

    @Nested
    class findBy {

        @Test
        @Sql("/test-get-item.sql")
        void 성공_파트너스_아이템_내림차순_조회() {
            // given
            Long id = 1L;
            Sort.Direction sortOrder = Sort.Direction.DESC;
            String sortProperty = SortCondition.NEW.getCondition();
            Sort sort = Sort.by(sortOrder, sortProperty);
            Pageable pageable = PageRequest.of(0, 50, sort);

            // when
            Page<Item> items = itemRepository.findAllByPartnersId(id, pageable);

            // then
            assertThat(items).hasSize(2);
            assertThat(items.getContent()).extracting("name").containsExactlyInAnyOrder("Item 1", "Item 2");
            assertThat(items.getContent()).extracting("brand.name").containsOnly("Brand A");
        }

        @Test
        @Sql("/test-get-item.sql")
        void 성공_브랜드의_모든아이템_내림차순_조회() {
            // given
            Long brandId = 1L;
            Sort.Direction sortOrder = Sort.Direction.DESC;
            String sortProperty = SortCondition.NEW.getCondition();
            Sort sort = Sort.by(sortOrder, sortProperty);
            Pageable pageable = PageRequest.of(0, 50, sort);

            // when
            Page<Item> items = itemRepository.findAllByBrandId(brandId, pageable);

            // then
            assertThat(items).hasSize(2);
            assertThat(items.getContent()).extracting("name").containsExactlyInAnyOrder("Item 1", "Item 2");
            assertThat(items.getContent()).extracting("brand.name").containsOnly("Brand A");
        }
    }

    @Nested
    class findByIdAndPartnersId {
        @Test
        @Sql("/test-get-item.sql")
        void 성공_파트너스_상품_상세조회() {
            // given
            Long partnersId = 1L;
            Long itemId = 1L;

            // when
            Optional<Item> item = itemRepository.findByIdAndPartnersId(itemId, partnersId);

            // then
            assertThat(item.isEmpty()).isFalse();
            assertThat(item.get().getId()).isEqualTo(itemId);
            assertThat(item.get().getName()).isEqualTo("Item 1");
            assertThat(item.get().getBrand().getPartners().getId()).isEqualTo(partnersId);
        }
    }
}