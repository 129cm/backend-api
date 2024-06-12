package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import com.d129cm.backendapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBrandController {

    private final MemberService memberService;

    @GetMapping("/members/brands/{brandId}")
    public ResponseEntity<CommonResponse<BrandsForMemberResponse>> getBrandsForMember(
            @RequestParam (required = false, defaultValue = "NEW") String sort,
            @RequestParam (required = false, defaultValue = "DESC") String sortOrder,
            @PathVariable Long brandId,
            @RequestParam (required = false, defaultValue = "0") int page,
            @RequestParam (required = false, defaultValue = "10") int size) {
        BrandsForMemberResponse brandsForMemberResponse = memberService.getBrandsForMember(sort, sortOrder, brandId, page, 50);
        return ResponseEntity.ok(CommonResponse.success(brandsForMemberResponse));
    }
}
