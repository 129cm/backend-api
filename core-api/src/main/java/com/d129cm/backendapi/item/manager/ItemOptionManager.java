package com.d129cm.backendapi.item.manager;

import com.d129cm.backendapi.item.domain.ItemOption;
import com.d129cm.backendapi.item.repository.ItemOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemOptionManager {

    private final ItemOptionRepository itemOptionRepository;

    public ItemOption getItemOption(Long itemOptionId) {
        return itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new EntityNotFoundException("일치하는 아이템 옵션이 없습니다."));
    }
}
