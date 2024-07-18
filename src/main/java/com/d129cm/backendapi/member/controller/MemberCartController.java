package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.CartItemRequest;
import com.d129cm.backendapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberCartController {

    private final MemberService memberService;

    @PostMapping("/members/carts")
    public ResponseEntity<CommonResponse<?>> addItemToCart(@RequestBody CartItemRequest request,
                                                           @AuthenticationPrincipal MemberDetails memberDetails) {
        memberService.addItemToCart(memberDetails.member(), request);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
