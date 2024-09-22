package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.MyOrderInfoResponse;
import com.d129cm.backendapi.member.dto.MyOrderResponse;
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

    @GetMapping("/orders/my-order")
    public ResponseEntity<CommonResponse<List<MyOrderResponse>>> getMyOrders(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<MyOrderResponse> myOrderResponse = memberOrderService.getMyOrders(memberDetails.member(), page, size);
        return ResponseEntity.ok(CommonResponse.success(myOrderResponse));
    }

    @GetMapping("/orders/my-order/{orderId}")
    public ResponseEntity<CommonResponse<MyOrderInfoResponse>> getMyOrderDetails(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MemberDetails memberDetails) {
        MyOrderInfoResponse myOrderInfoResponse = memberOrderService.getMyOrderDetails(memberDetails.member(), orderId);
        return ResponseEntity.ok(CommonResponse.success(myOrderInfoResponse));
    }

    @PostMapping("/orders/{orderId}/{itemOptionId}")
    public ResponseEntity<CommonResponse<?>> withdrawOrder(
            @PathVariable Long orderId,
            @PathVariable Long itemOptionId) {
        memberOrderService.withdrawOrder(orderId, itemOptionId);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
