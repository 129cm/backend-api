package com.d129cm.backendapi.member.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.member.dto.ItemForMemberResponse;
import com.d129cm.backendapi.member.service.MemberItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberItemController {
    private final MemberItemService memberItemService;

    @GetMapping("/members/items/{itemId}")
    public ResponseEntity<CommonResponse<ItemForMemberResponse>> getItemForMember(@PathVariable Long itemId) {
        ItemForMemberResponse itemForMemberResponse = memberItemService.getItemForMember(itemId);
        return ResponseEntity.ok(CommonResponse.success(itemForMemberResponse));
    }
}
