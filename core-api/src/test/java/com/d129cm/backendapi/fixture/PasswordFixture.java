package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.common.domain.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordFixture {
    private PasswordFixture() {}
    public static Password createPassword() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        return Password.of(rawPassword, passwordEncoder);
    }
}
