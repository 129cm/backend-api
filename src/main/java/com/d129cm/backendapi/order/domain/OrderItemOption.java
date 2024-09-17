package com.d129cm.backendapi.order.domain;

import com.d129cm.backendapi.common.domain.BaseEntity;
import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.d129cm.backendapi.item.domain.ItemOption;
import com.mysema.commons.lang.Assert;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "codeId", column = @Column(name = "code_id")),
            @AttributeOverride(name = "groupId", column = @Column(name = "group_id"))
    })
    private CommonCodeId commonCodeId;

    private Integer count;

    private Integer salesPrice;

    @Builder
    private OrderItemOption(Order order, ItemOption itemOption, CommonCodeId commonCodeId, Integer count, Integer salesPrice) {
        Assert.isTrue(GroupName.주문.getGroupId().equals(commonCodeId.getGroupId()), "올바른 그룹 코드가 아닙니다.");

        this.id = new OrderItemOptionId(order.getId(), itemOption.getId());
        this.order = order;
        this.itemOption = itemOption;
        this.commonCodeId = commonCodeId;
        this.count = count;
        this.salesPrice = salesPrice;
    }

    public void changeOrderState(CommonCodeId commonCodeId) {
        this.commonCodeId = commonCodeId;
    }
}

