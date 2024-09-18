package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
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
    private String orderState;

    @QueryProjection
    public OrderItemDto(Integer count, String itemImage, String itemName, Long itemOptionId, String itemOptionName, String orderState) {
        this.count = count;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemOptionId = itemOptionId;
        this.itemOptionName = itemOptionName;
        this.orderState = CodeName.from(orderState, GroupName.주문);
    }
}
