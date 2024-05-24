package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.exception.BaseException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnersService {

    private final PartnersRepository partnersRepository;

    public void savePartners(PartnersSignupRequest request) {
        if (partnersRepository.existsByEmail(request.email())) {
            throw new BaseException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT.value());
        }

        Partners newPartners = request.toPartnersEntity();

        partnersRepository.save(newPartners);
    }
}
