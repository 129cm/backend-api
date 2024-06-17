package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandManager {

    private final BrandRepository brandRepository;

    public boolean existByBrandName(String brandName) {
        return brandRepository.existsByName(brandName);
    }
}