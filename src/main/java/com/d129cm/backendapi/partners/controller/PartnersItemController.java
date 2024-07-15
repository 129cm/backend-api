package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.GetItemsForPartnersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PartnersItemController {

    private final ItemService itemService;

    @PostMapping("/partners/brands/items")
    public ResponseEntity<CommonResponse<?>> createItem(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @RequestBody ItemCreateRequest request) {

        itemService.createItem(partners, request);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/partners/brands/items")
    public ResponseEntity<CommonResponse<?>> getItemForPartners(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @RequestParam(required = false, defaultValue = "NEW") SortCondition sort,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction sortOrder,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        GetItemsForPartnersResponse response = itemService.getItemsForPartners(partners, sort, sortOrder, page);
        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
