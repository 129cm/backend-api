package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersMyPageResponse;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.service.PartnersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartnersController {

    private final PartnersService partnersService;

    @PostMapping("/partners/signup")
    public ResponseEntity<CommonResponse<Void>> signup(@Valid @RequestBody PartnersSignupRequest request) {
        partnersService.savePartners(request);

        return ResponseEntity.ok().body(CommonResponse.success());
    }

    @GetMapping("/partners")
    public ResponseEntity<CommonResponse<PartnersMyPageResponse>> getPartnersMyPage(
            @AuthenticationPrincipal(expression = "partners") Partners partners) {
        PartnersMyPageResponse response = partnersService.getPartnersMyPage(partners);

        return ResponseEntity.ok().body(CommonResponse.success(response));
    }
}
