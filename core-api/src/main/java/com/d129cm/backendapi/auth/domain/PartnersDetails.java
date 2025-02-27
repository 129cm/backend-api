package com.d129cm.backendapi.auth.domain;

import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record PartnersDetails(Partners partners) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(Role.ROLE_PARTNERS);
    }

    @Override
    public String getPassword() {
        return partners.getPassword().getPassword();
    }

    @Override
    public String getUsername() {
        return partners.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
