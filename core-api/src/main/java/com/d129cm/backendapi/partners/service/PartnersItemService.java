package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.partners.dto.GetItemDetailsResponse;
import com.d129cm.backendapi.partners.dto.PutItemDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnersItemService {

    private final ItemManager itemManager;

    public GetItemDetailsResponse getItemDetails(Long itemId, Long partnersId) {
        Item item = itemManager.getItemByIdAndPartnersId(itemId, partnersId);
        return GetItemDetailsResponse.of(item);
    }

    @Transactional
    public void putItemDetails(Long partnersId, Long itemId, PutItemDetailsRequest request) {
        Item oldItem = itemManager.getItemByIdAndPartnersId(itemId, partnersId);
        Item newItem = request.toItemEntity();

        oldItem.updateItem(newItem);
    }

    @Transactional
    public void deleteItem(Long partnersId, Long itemId) {
        Item item = itemManager.getItemByIdAndPartnersId(itemId, partnersId);

        itemManager.deleteItem(item);
    }
}
