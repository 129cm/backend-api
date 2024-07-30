package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.CartForMemberResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.dto.CartItemUpdateRequest;
import com.d129cm.backendapi.member.service.MemberCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberCartController {

    private final MemberCartService memberCartService;

    @PostMapping("/members/carts")
    public ResponseEntity<CommonResponse<?>> addItemToCart(@RequestBody CartItemRequest request,
                                                           @AuthenticationPrincipal MemberDetails memberDetails) {
        memberCartService.addItemToCart(memberDetails.member(), request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/members/carts")
    public ResponseEntity<CommonResponse<?>> getCart(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<CartForMemberResponse> cartForMemberResponses =  memberCartService.getCart(memberDetails.member());
        return ResponseEntity.ok(CommonResponse.success(cartForMemberResponses));
    }

    @PutMapping("/members/carts")
    public ResponseEntity<CommonResponse<?>> updateItemQuantityInCart(@RequestBody CartItemUpdateRequest request,
            @AuthenticationPrincipal MemberDetails memberDetails) {
        memberCartService.updateItemQuantityInCart(memberDetails.member(), request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @DeleteMapping("/members/carts/{itemId}/{itemOptionId}")
    public ResponseEntity<CommonResponse<?>> deleteItemFromCart(@PathVariable Long itemId, @PathVariable Long itemOptionId,
                                                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        memberCartService.deleteItemFromCart(memberDetails.member(), itemId, itemOptionId);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
