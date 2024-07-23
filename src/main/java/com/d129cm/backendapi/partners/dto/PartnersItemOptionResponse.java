package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.item.domain.ItemOption;

public record PartnersItemOptionResponse(
        Long itemOptionId,
        String itemOptionName,
        Integer itemOptionQuantity,
        Integer itemOptionPrice
) {
    public static PartnersItemOptionResponse of(ItemOption itemOption) {
        return new PartnersItemOptionResponse(
                itemOption.getId(),
                itemOption.getName(),
                itemOption.getQuantity(),
                itemOption.getOptionPrice()
        );
    }
}
