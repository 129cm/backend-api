package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.service.PartnersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(HttpStatus.CREATED, null));
    }
}
