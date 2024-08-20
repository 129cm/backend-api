package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.item.domain.ItemOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOption extends BaseEntity {

    @EmbeddedId
    private OrderItemOptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemOptionId")
    private ItemOption itemOption;

    private Integer count;
    private Integer salesPrice;

    @Builder
    private OrderItemOption(Order order, ItemOption itemOption, Integer count, Integer salesPrice) {
        this.id = new OrderItemOptionId(order.getId(), itemOption.getId());
        this.order = order;
        this.itemOption = itemOption;
        this.count = count;
        this.salesPrice = salesPrice;
    }
}
