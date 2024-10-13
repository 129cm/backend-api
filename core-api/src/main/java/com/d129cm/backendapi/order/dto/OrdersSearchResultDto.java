package com.d129cm.backendapi.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrdersSearchResultDto {
    private List<OrdersSearchResponseDto> orders;
    private long total;

    public OrdersSearchResultDto(List<OrdersSearchResponseDto> orders, long total) {
        this.orders = orders;
        this.total = total;
    }
}
