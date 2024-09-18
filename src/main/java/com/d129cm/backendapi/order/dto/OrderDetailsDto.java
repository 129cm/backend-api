package com.d129cm.backendapi.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailsDto {
    private Long orderId;
    private LocalDateTime orderCreatedAt;
    private List<OrderItemDetailsDto> itemDetails;
    private Long memberId;
    private String memberName;
    private OrderAddressDto address;

    @QueryProjection
    public OrderDetailsDto(Long orderId, LocalDateTime orderCreatedAt, List<OrderItemDetailsDto> itemDetails, Long memberId, String memberName, OrderAddressDto address) {
        this.orderId = orderId;
        this.orderCreatedAt = orderCreatedAt;
        this.itemDetails = itemDetails;
        this.memberId = memberId;
        this.memberName = memberName;
        this.address = address;
    }
}
