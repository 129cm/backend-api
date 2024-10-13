package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.repository.BrandRepository;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.partners.domain.Partners;
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

    public Brand getBrandWithItems(Partners partners) {
        return brandRepository.findByPartners(partners)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 브랜드가 없습니다."));
    }

    public boolean existByBrandName(String brandName) {
        return brandRepository.existsByName(brandName);
    }

    public void updateBrandItem(Brand brand, Item item) {
        brand.addItem(item);
        brandRepository.save(brand);
    }
}