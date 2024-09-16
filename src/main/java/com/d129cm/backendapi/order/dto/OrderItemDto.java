package com.d129cm.backendapi.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderItemDto {
    private String itemName;
    private String itemImage;
    private Long itemOptionId;
    private String itemOptionName;
    private Integer count;

    @QueryProjection
    public OrderItemDto(Integer count, String itemImage, String itemName, Long itemOptionId, String itemOptionName) {
        this.count = count;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemOptionId = itemOptionId;
        this.itemOptionName = itemOptionName;
    }
}
