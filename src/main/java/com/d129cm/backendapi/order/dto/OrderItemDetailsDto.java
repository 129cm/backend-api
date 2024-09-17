package com.d129cm.backendapi.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderItemDetailsDto {
    private Long itemId;
    private String itemName;
    private String itemImage;
    private Long ItemOptionId;
    private String ItemOptionName;
    private Integer salePrice;
    private Integer count;

    @QueryProjection
    public OrderItemDetailsDto(Long itemId, String itemName, String itemImage, Long itemOptionId, String itemOptionName, Integer count, Integer salePrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.ItemOptionId = itemOptionId;
        this.ItemOptionName = itemOptionName;
        this.count = count;
        this.salePrice = salePrice;
    }
}
