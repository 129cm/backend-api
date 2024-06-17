package com.d129cm.backendapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Profile("test")
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public UserDetailsService inMemoryDetailsService() {
        UserDetails partners = User.withUsername("partners")
                .password("{noop}password") // {noop}은 비밀번호를 인코딩하지 않음을 나타냅니다.
                .roles("PARTNERS")
                .build();

        UserDetails members = User.withUsername("members")
                .password("{noop}password")
                .roles("MEMBERS")
                .build();
        return new InMemoryUserDetailsManager(partners, members);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
