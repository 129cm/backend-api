package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;

import java.util.List;

public record CreateOrderDto(
        List<BrandsForOrderResponse> brandsForOrderResponse
) {
}
