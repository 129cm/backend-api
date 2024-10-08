package com.d129cm.backendapi.auth.service;

import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnersDetailsService implements UserDetailsService {

    private final PartnersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Partners partners = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new PartnersDetails(partners);
    }
}
