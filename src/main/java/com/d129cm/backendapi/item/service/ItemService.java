package com.d129cm.backendapi.item.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.partners.domain.Partners;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final BrandManager brandManager;

    public void createItem(Partners partners, ItemCreateRequest request) {
        Item newItem = request.toItemEntity();
        Brand brand = partners.getBrand();

        brandManager.updateBrandItem(brand, newItem);
    }
}
