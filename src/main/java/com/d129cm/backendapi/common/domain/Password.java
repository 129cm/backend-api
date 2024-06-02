package com.d129cm.backendapi.common.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Embeddable
public class Password {
    private String value;
    protected Password() {
    }

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return new Password(passwordEncoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.value);
    }
}
