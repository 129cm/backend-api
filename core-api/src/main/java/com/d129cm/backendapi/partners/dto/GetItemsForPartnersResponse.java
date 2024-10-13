package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.Item;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record GetItemsForPartnersResponse(
        List<ItemForPartnersResponse> itemResponses
) {

    public static GetItemsForPartnersResponse of(Page<Item> items) {
        List<ItemForPartnersResponse> itemsResponse = items.stream().map(item -> new ItemForPartnersResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getImage(),
                item.getBrand().getName(),
                item.getModifiedAt())).collect(Collectors.toList());

        return new GetItemsForPartnersResponse(itemsResponse);
    }
}
