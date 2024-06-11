package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemManager {
    private final ItemRepository itemRepository;

    public List<Item> getAllItemByBrandId(Long brandId) {
        return itemRepository.findAllByBrandId(brandId);
    }
}
