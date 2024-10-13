package com.d129cm.backendapi.member.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MyOrderResponse(
        Long orderId,
        String orderSerial,
        LocalDateTime orderDate,
        List<MyOrderDetailsResponse> itemInfoList
) {

}
