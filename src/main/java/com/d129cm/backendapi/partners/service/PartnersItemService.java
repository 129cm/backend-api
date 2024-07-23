package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.partners.dto.PartnersItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnersItemService {

    private final ItemManager itemManager;

    public PartnersItemResponseDto getItemDetails(Long itemId, Long partnersId) {
        Item item = itemManager.getItemByIdAndPartnersId(itemId, partnersId);

        return PartnersItemResponseDto.of(item);
    }
}
