package com.d129cm.backendapi.partners.dto;

import com.d129cm.backendapi.partners.domain.Partners;

public record PartnersSignupRequest(
        String email,
        String password,
        String businessNumber) {
    public Partners toPartnersEntity() {
        return Partners.builder()
                .email(email)
                .password(password)
                .businessNumber(businessNumber)
                .build();
    }
}
