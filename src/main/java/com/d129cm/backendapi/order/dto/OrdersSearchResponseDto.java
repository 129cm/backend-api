package com.d129cm.backendapi.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrdersSearchResponseDto {
    private Long memberId;
    private String memberName;
    private LocalDateTime orderCreatedAt;
    private Long orderId;
    private List<OrderItemDto> orderItems;
    private String orderState;

    @QueryProjection
    public OrdersSearchResponseDto(Long memberId, String memberName, LocalDateTime orderCreatedAt, Long orderId, List<OrderItemDto> orderItems, String orderState) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.orderCreatedAt = orderCreatedAt;
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.orderState = orderState;
    }
}
