package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.item.domain.SortCondition;
import com.d129cm.backendapi.item.dto.ItemCreateRequest;
import com.d129cm.backendapi.item.service.ItemService;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.GetItemDetailsResponse;
import com.d129cm.backendapi.partners.dto.GetItemsForPartnersResponse;
import com.d129cm.backendapi.partners.dto.PutItemDetailsRequest;
import com.d129cm.backendapi.partners.service.PartnersItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PartnersItemController {

    private final ItemService itemService;
    private final PartnersItemService partnersItemService;

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

    @GetMapping("/partners/brands/items/{itemId}")
    public ResponseEntity<CommonResponse<?>> getItemDetailsForPartners(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @PathVariable Long itemId
    ) {
        GetItemDetailsResponse response = partnersItemService.getItemDetails(itemId, partners.getId());
        CommonResponse<GetItemDetailsResponse> success = CommonResponse.success(response);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/partners/brands/items/{itemId}")
    public ResponseEntity<CommonResponse<?>> putItemDetails(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @PathVariable Long itemId,
            @RequestBody PutItemDetailsRequest request
    ) {
        partnersItemService.putItemDetails(partners.getId(), itemId, request);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
