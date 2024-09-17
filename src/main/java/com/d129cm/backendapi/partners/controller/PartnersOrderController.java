package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import com.d129cm.backendapi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partners/brands/orders")
public class PartnersOrderController {

    private final OrderService orderService;

    @GetMapping
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

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable("orderId") Long orderId) {
        OrderDetailsDto response = orderService.getOrderDetailsByOrderId(orderId);

        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
