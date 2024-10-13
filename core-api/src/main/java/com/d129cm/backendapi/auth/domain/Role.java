package com.d129cm.backendapi.auth.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_MEMBER, ROLE_PARTNERS;

    @Override
    public String getAuthority() {
        return name();
    }
}
