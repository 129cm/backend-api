package com.d129cm.backendapi.order.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOptionId implements Serializable {
    private Long orderId;
    private Long itemOptionId;

    public OrderItemOptionId(Long orderId, Long itemOptionId) {
        this.orderId = orderId;
        this.itemOptionId = itemOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemOptionId that = (OrderItemOptionId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(itemOptionId, that.itemOptionId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(orderId);
        result = 31 * result + Objects.hashCode(itemOptionId);
        return result;
    }
}
