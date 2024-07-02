package com.d129cm.backendapi.partners.manager;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartnersManager {
    private final PartnersRepository partnersRepository;

    public void updatePartnersBrand(Partners partners, Brand brand) {
        partners.setBrand(brand);
        partnersRepository.save(partners);
    }
}