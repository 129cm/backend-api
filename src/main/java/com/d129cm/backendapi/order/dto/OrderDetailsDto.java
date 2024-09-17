package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.domain.code.GroupName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailsDto {
    private Long orderId;
    private String orderState;
    private LocalDateTime orderCreatedAt;
    private List<OrderItemDetailsDto> itemDetails;
    private Long memberId;
    private String memberName;
    private OrderAddressDto address;

    @QueryProjection
    public OrderDetailsDto(Long orderId, String orderState, LocalDateTime orderCreatedAt, List<OrderItemDetailsDto> itemDetails, Long memberId, String memberName, OrderAddressDto address) {
        this.orderId = orderId;
        this.orderState = CodeName.from(orderState, GroupName.배송);
        this.orderCreatedAt = orderCreatedAt;
        this.itemDetails = itemDetails;
        this.memberId = memberId;
        this.memberName = memberName;
        this.address = address;
    }
}
