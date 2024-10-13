package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.OrderAddressDto;

import java.time.LocalDateTime;
import java.util.List;

public record MyOrderInfoResponse(
        String orderSerial,
        LocalDateTime orderDate,
        List<MyOrderDetailsResponse> itemInfoList,
        OrderInfoResponse orderInfo,
        OrderAddressDto address
) {
        public static MyOrderInfoResponse of(Order order, List<MyOrderDetailsResponse> itemInfoList, OrderInfoResponse orderInfo) {
            OrderAddressDto address = new OrderAddressDto(order.getMember());
            return new MyOrderInfoResponse(
                    order.getOrderSerial(),
                    order.getCreatedAt(),
                    itemInfoList,
                    orderInfo,
                    address
            );
        }
}
