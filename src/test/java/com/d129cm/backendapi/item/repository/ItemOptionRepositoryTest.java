package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.config.InitializeTestContainers;
import com.d129cm.backendapi.item.domain.ItemOption;
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
public class ItemOptionRepositoryTest {

    @Autowired
    ItemOptionRepository itemOptionRepository;

    @Nested
    class create {
        @Test
        void 성공_아이템_저장() {
            //given
            ItemOption itemOption = ItemOption.builder()
                    .name("상품 옵션 이름")
                    .quantity(100)
                    .build();

            // when
            ItemOption savedItemOption = itemOptionRepository.save(itemOption);

            // then
            Assertions.assertAll(
                    () -> assertThat(savedItemOption).isNotNull(),
                    () -> assertThat(savedItemOption.getId()).isEqualTo(itemOption.getId()),
                    () -> assertThat(savedItemOption.getName()).isEqualTo(itemOption.getName()),
                    () -> assertThat(savedItemOption.getQuantity()).isEqualTo(itemOption.getQuantity())
            );
        }
    }
}
