package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.partners.domain.Partners;

import static org.mockito.Mockito.spy;

public class BrandFixture {

    public static Brand createBrand(Partners partners) {
        Brand brand = spy(Brand.builder()
                .name("브랜드 이름")
                .description("브랜드 설명")
                .image("브랜드 이미지")
                .partners(partners)
                .build());
        return brand;
    }
}
