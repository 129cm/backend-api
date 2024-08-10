package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.partners.domain.Partners;

import static org.mockito.Mockito.spy;

public class PartnersFixture {
    private PartnersFixture() {}
    public static Partners createPartners(String email, String businessNumber) {
        Password password = PasswordFixture.createPassword();
        Partners partners = spy(Partners.builder()
                .email(email)
                .businessNumber(businessNumber)
                .password(password)
                .build());
        return partners;
    }
}
