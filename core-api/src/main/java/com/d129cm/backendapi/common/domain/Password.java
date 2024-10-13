package com.d129cm.backendapi.common.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Embeddable
public class Password {
    private String password;
    protected Password() {
    }

    private Password(String password) {
        this.password = password;
    }

    public static Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return new Password(passwordEncoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
