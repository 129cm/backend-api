package com.d129cm.backendapi.brand.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void init() {
        Brand testBrand = Brand.builder()
                .name("testBrand")
                .description("testDescription")
                .image("testImage")
                .build();

        brandRepository.save(testBrand);
    }

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

    @Test
    void true_브랜드_이름으로_존재유무_확인() {
        // given
        String brandName = "testBrand";

        // when
        boolean isExist = brandRepository.existsByName(brandName);

        // then
        assertThat(isExist).isTrue();
    }
}
