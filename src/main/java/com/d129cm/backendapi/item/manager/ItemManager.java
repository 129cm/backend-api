package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemManager {

    private final ItemRepository itemRepository;

    public Page<Item> getAllItemByBrandId(Long brandId, Pageable pageable) {
        return itemRepository.findAllByBrandId(brandId, pageable);
    }

    public Sort createItemSort(SortCondition sort, Sort.Direction sortOrder) {
        String sortProperty = sort.getCondition();
        return Sort.by(sortOrder, sortProperty);
    }

    public void createItem(Item item) {
        itemRepository.save(item);
    }
}
