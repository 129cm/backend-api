package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.BrandsForMemberResponse;
import com.d129cm.backendapi.member.dto.MemberMyPageResponse;
import com.d129cm.backendapi.member.dto.MemberSignupRequest;
import com.d129cm.backendapi.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<CommonResponse<Void>> signup(@Valid @RequestBody MemberSignupRequest request) {
        memberService.saveMember(request);

        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/members")
    public ResponseEntity<CommonResponse<MemberMyPageResponse>> getMemberMyPage(@AuthenticationPrincipal MemberDetails memberDetails) {
        MemberMyPageResponse memberMyPageResponse = memberService.getMemberMyPage(memberDetails);
        return ResponseEntity.ok(CommonResponse.success(memberMyPageResponse));
    }

}
