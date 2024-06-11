package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.repository.BrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandManager {
    private final BrandRepository brandRepository;

    public Brand getBrand(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 브랜드가 없습니다."));
    }
}
