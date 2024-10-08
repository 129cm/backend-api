package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.order.domain.OrderItemOptionId;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrderItemOptionIdDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.manager.OrderItemOptionManager;
import com.d129cm.backendapi.order.manager.OrderManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnersOrderService {

    private final OrderManager orderManager;
    private final OrderItemOptionManager orderItemOptionManager;

    public OrdersSearchResultDto searchResult(String itemName, String startDate, String endDate, String orderState, int size, int page) {
        return orderManager.searchResult(itemName, startDate, endDate, orderState, size, page);
    }

    public OrderDetailsDto getOrderDetailsByOrderId(Long orderId) {
        return orderManager.getOrderDetailsByOrderId(orderId);
    }

    public int acceptOrders(List<OrderItemOptionIdDto> requests) {
        List<OrderItemOptionId> ids = requests.stream().map(OrderItemOptionIdDto::toOrderItemOptionId).toList();
        return orderItemOptionManager.modifyOrderState(CodeName.주문완료.getCodeId(), ids);
    }

    public int cancelOrders(List<OrderItemOptionIdDto> requests) {
        List<OrderItemOptionId> ids = requests.stream().map(OrderItemOptionIdDto::toOrderItemOptionId).toList();
        return orderItemOptionManager.modifyOrderState(CodeName.주문취소.getCodeId(), ids);
    }
}
