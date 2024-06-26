package com.d129cm.backendapi.brand.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.repository.BrandRepository;
import com.d129cm.backendapi.item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandManager {

    private final BrandRepository brandRepository;

    public boolean existByBrandName(String brandName) {
        return brandRepository.existsByName(brandName);
    }

    public void updateBrandItem(Brand brand, Item item){
        item.updateBrand(brand);
        brandRepository.save(brand);
    }
}