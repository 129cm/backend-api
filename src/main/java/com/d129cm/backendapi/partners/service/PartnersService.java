package com.d129cm.backendapi.partners.service;

import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnersService {

    private final PartnersRepository partnersRepository;
    private final PasswordEncoder passwordEncoder;

    public void savePartners(PartnersSignupRequest request) {
        if (partnersRepository.existsByEmail(request.email())) {
            throw new ConflictException("email", request.email());
        }

        Partners newPartners = request.toPartnersEntity();
        newPartners.encodePassword(passwordEncoder);

        partnersRepository.save(newPartners);
    }
}
