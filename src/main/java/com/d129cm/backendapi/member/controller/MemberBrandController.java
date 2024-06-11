package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import com.d129cm.backendapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBrandController {

    private final MemberService memberService;

    @GetMapping("/members/brands/{brandId}")
    public ResponseEntity<CommonResponse<BrandsForMemberResponse>> getBrandsForMember(@PathVariable Long brandId) {
        BrandsForMemberResponse brandsForMemberResponse = memberService.getBrandsForMember(brandId);
        return ResponseEntity.ok(CommonResponse.success(brandsForMemberResponse));
    }
}
