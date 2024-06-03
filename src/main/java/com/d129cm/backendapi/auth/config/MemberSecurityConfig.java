package com.d129cm.backendapi.auth.config;

import com.d129cm.backendapi.auth.filter.JwtAuthorizationFilter;
import com.d129cm.backendapi.auth.filter.MemberJwtLoginFilter;
import com.d129cm.backendapi.auth.service.MemberDetailsService;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MemberSecurityConfig {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberDetailsService memberDetailsService;

    @Bean
    public AuthenticationProvider memberProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(memberDetailsService);
        return provider;
    }

    @Bean
    @Primary
    public AuthenticationManager memberAuthenticationManager() {
        return new ProviderManager(memberProvider());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setMaxAge(60L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity http) throws Exception {
        final RequestMatcher ignoredRequests = new OrRequestMatcher(
                List.of(new AntPathRequestMatcher("/members/signup"))
        );

        http.securityMatcher("/members/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(common -> common
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll());

        http.authorizeHttpRequests(member -> member
                .requestMatchers(HttpMethod.POST, "/members/signup", "/members/login").permitAll()
                .anyRequest().hasRole("MEMBER"));

        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new MemberJwtLoginFilter(jwtProvider, memberAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(jwtProvider, memberDetailsService, ignoredRequests), MemberJwtLoginFilter.class);

        return http.build();
    }
}
