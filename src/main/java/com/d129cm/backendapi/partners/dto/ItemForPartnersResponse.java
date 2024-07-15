package com.d129cm.backendapi.partners.dto;

import java.time.LocalDateTime;

public record ItemForPartnersResponse(
        Long itemId,
        String itemName,
        Integer price,
        String itemImage,
        String brandName,
        LocalDateTime itemModifiedAt
) {

}
