package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBrandService {

    private final BrandManager brandManager;
    private final ItemManager itemManager;

    public BrandsForMemberResponse getBrandsForMember(SortCondition sort, Sort.Direction sortOrder, Long brandId, int page, int size) {
        Brand brand =brandManager.getBrand(brandId);
        Sort sortObj = itemManager.createItemSort(sort, sortOrder);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Item> items = itemManager.getAllItemByBrandId(brandId, pageable);
        return BrandsForMemberResponse.of(brand, items.getContent());
    }
}
