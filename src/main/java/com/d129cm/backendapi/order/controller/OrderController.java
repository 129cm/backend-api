package com.d129cm.backendapi.order.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<CommonResponse<String>> createOrder(@RequestBody CreateOrderDto createOrderDto, @AuthenticationPrincipal MemberDetails memberDetails) throws Exception {
        String orderId = orderService.createOrder(createOrderDto, memberDetails.member());
        return ResponseEntity.ok().body(CommonResponse.success(orderId));
    }
}
