package com.d129cm.backendapi.brand.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BrandItemController {

    private final ItemService itemService;

    @PostMapping("/partners/brands/items")
    public ResponseEntity<CommonResponse<?>> createItem(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @RequestBody ItemCreateRequest request) {

        itemService.createItem(partners, request);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
