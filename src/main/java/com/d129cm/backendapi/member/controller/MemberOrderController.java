package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.OrderFormForMemberResponse;
import com.d129cm.backendapi.member.service.MemberOrderService;
import com.d129cm.backendapi.order.dto.CreateOrderDto;
import com.d129cm.backendapi.order.dto.OrderFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberOrderController {

    private final MemberOrderService memberOrderService;

    @GetMapping("/orders/order-form")
    public ResponseEntity<CommonResponse<OrderFormForMemberResponse>>  getOrderForm(
            @RequestBody List<OrderFormDto> orderFormDto,
            @AuthenticationPrincipal MemberDetails memberDetails) {
        OrderFormForMemberResponse orderFormForMemberResponse =
                memberOrderService.getOrderForm(orderFormDto, memberDetails.member());
        return ResponseEntity.ok(CommonResponse.success(orderFormForMemberResponse));
    }

    @PostMapping("/orders")
    public ResponseEntity<CommonResponse<String>> createOrder(@RequestBody CreateOrderDto createOrderDto, @AuthenticationPrincipal MemberDetails memberDetails) throws Exception {
        String orderId = memberOrderService.createOrder(createOrderDto, memberDetails.member());
        return ResponseEntity.ok().body(CommonResponse.success(orderId));
    }
}
