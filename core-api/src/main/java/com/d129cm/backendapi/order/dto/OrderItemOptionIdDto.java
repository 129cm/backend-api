package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.order.domain.OrderItemOptionId;

public record OrderItemOptionIdDto(
        Long orderId,
        Long itemOptionId
) {
    public OrderItemOptionId toOrderItemOptionId() {
        return new OrderItemOptionId(orderId, itemOptionId);
    }
}
