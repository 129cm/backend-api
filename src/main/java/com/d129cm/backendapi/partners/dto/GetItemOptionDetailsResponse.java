package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.ItemOption;

public record GetItemOptionDetailsResponse(
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionQuantity,
        Integer itemOptionPrice
) {
    public static GetItemOptionDetailsResponse of(ItemOption itemOption) {
        return new GetItemOptionDetailsResponse(
                itemOption.getId(),
                itemOption.getName(),
                itemOption.getQuantity(),
                itemOption.getOptionPrice()
        );
    }
}
