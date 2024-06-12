package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.Item;
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

    public Sort getSortObject(String sort, String sortOrder) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortProperty = "";
        switch (sort.toUpperCase()) {
            case "PRICE" -> sortProperty = "price";
            case "SALES" -> sortProperty = "sales";
            default -> sortProperty = "createdAt";
        }
        return Sort.by(direction, sortProperty);
    }
}
