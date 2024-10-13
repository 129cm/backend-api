package com.d129cm.backendapi.member.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.member.dto.ItemForMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class MemberItemService {
    private final ItemManager itemManager;

    public ItemForMemberResponse getItemForMember(Long itemId) {
        Item item = itemManager.getItem(itemId);
        Brand brand = item.getBrand();
        List<ItemOption> itemOptions = item.getItemOptions();
        return ItemForMemberResponse.of(brand, item, itemOptions);
    }
}
