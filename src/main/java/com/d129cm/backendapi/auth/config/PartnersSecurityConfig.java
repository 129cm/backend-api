package com.d129cm.backendapi.auth.config;

import com.d129cm.backendapi.auth.filter.CustomAccessDeniedHandler;
import com.d129cm.backendapi.auth.filter.CustomAuthenticationEntryPoint;
import com.d129cm.backendapi.auth.filter.JwtAuthorizationFilter;
import com.d129cm.backendapi.auth.filter.JwtLoginFilter;
import com.d129cm.backendapi.auth.service.PartnersDetailsService;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class PartnersSecurityConfig {
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final PartnersDetailsService partnersDetailsService;

    @Bean
    public AuthenticationProvider partnersProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(partnersDetailsService);
        return provider;
    }

    @Bean
    @Order(2)
    public AuthenticationManager partnersAuthenticationManager() {
        return new ProviderManager(partnersProvider());
    }

    @Bean
    @Order(2)
    public SecurityFilterChain partnersSecurityFilterChain(HttpSecurity http) throws Exception {
        final RequestMatcher ignoredRequests = new OrRequestMatcher(
                List.of(new AntPathRequestMatcher("/partners/signup", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/partners/login", HttpMethod.POST.name())
                ));

        http.securityMatcher("/partners/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(common -> common
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(ignoredRequests).permitAll()
                .anyRequest().hasRole("PARTNERS"));

        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new JwtLoginFilter(jwtProvider, partnersAuthenticationManager(), "/partners/login"), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(jwtProvider, partnersDetailsService, ignoredRequests), JwtLoginFilter.class);

        http.exceptionHandling(exception -> exception
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }
}
