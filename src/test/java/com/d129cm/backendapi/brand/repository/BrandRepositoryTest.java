package com.d129cm.backendapi.brand.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.config.InitializeTestContainers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ImportTestcontainers(InitializeTestContainers.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void 성공_브랜드_저장() {
        // given
        Brand brand = Brand.builder()
                .name("brand")
                .image("image")
                .description("description")
                .build();

        // when
        Brand savedBrand = brandRepository.save(brand);

        // then
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isEqualTo(brand.getId());

    }
}
