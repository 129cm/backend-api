package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartnersOrderController {

    private final OrderService orderService;

    @GetMapping("/partners/brands/orders")
    public ResponseEntity<?> searchOrders(
            @RequestParam String itemName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String orderState,
            @RequestParam int size,
            @RequestParam int page) {
        OrdersSearchResultDto response = orderService.searchResult(itemName, startDate, endDate, orderState, size, page);
        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
