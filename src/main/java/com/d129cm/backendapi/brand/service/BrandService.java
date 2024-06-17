package com.d129cm.backendapi.brand.service;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.brand.dto.BrandCreateRequest;
import com.d129cm.backendapi.brand.manager.BrandManager;
import com.d129cm.backendapi.common.exception.BadRequestException;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.manager.PartnersManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {

    private final PartnersManager partnersManager;
    private final BrandManager brandManager;

    public void createBrand(Partners partners, BrandCreateRequest request){
        if(partners.getBrand() != null){
            throw BadRequestException.relationAlreadyExist("Brand");
        }

        Brand brand = request.toBrandEntity();

        if(brandManager.existByBrandName(brand.getName())){
            throw ConflictException.duplicatedValue("Brand", brand.getName());
        }

        partnersManager.updatePartnersBrand(partners, brand);
    }
}
