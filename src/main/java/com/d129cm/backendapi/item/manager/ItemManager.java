package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemManager {

    private final ItemRepository itemRepository;

    public void saveItem(Item item){
        itemRepository.save(item);
    }
}
