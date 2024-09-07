package com.d129cm.backendapi.order.dto;

import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.member.dto.BrandsForOrderResponse;

import java.util.List;

public record CreateOrderDto (
        Long orderMemberId,
        String receiverName,
        Address receiverAddress,
        List<BrandsForOrderResponse> brandsForOrderResponse
) {
}
