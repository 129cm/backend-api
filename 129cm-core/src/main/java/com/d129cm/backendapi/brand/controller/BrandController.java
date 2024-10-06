package com.d129cm.backendapi.brand.controller;

import com.d129cm.backendapi.brand.dto.BrandCreateRequest;
import com.d129cm.backendapi.brand.service.BrandService;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.partners.domain.Partners;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partners/brands")
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> createBrand(
            @AuthenticationPrincipal(expression = "partners") Partners partners,
            @RequestBody BrandCreateRequest request){
        brandService.createBrand(partners, request);

        return ResponseEntity.ok(CommonResponse.success());
    }
}
