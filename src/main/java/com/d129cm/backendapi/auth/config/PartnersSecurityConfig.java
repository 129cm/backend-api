package com.d129cm.backendapi.auth.config;

import com.d129cm.backendapi.auth.filter.JwtAuthorizationFilter;
import com.d129cm.backendapi.auth.filter.PartnersJwtLoginFilter;
import com.d129cm.backendapi.auth.service.PartnersUserDetailsService;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class PartnersSecurityConfig {

    private final PartnersUserDetailsService partnersUserDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider partnersProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(partnersUserDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager partnersAuthenticationManager() {
        return new ProviderManager(partnersProvider());
    }

    @Bean
    public PartnersJwtLoginFilter partnersJwtLoginFilter() {
        return new PartnersJwtLoginFilter(partnersAuthenticationManager());
    }

    @Bean
    public JwtAuthorizationFilter partnersJwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, partnersUserDetailsService);
    }

    @Bean("partnersSecurityFilterChain")
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/partners/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(common -> common
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/partners/signup", "partners/login").permitAll()
                .anyRequest().hasRole("PARTNERS"));

        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(partnersJwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(partnersJwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
