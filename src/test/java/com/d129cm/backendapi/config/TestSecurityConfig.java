package com.d129cm.backendapi.config;

import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

@Profile("test")
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public UserDetailsService inMemoryTestDetailsService() {
        Partners testPartners = Partners.builder()
                .email("testPartners@email.com")
                .password(mock(Password.class))
                .businessNumber("123-45-67890")
                .build();

        Member testMember = Member.builder()
                .email("testMember@email.com")
                .password(mock(Password.class))
                .name("testMember")
                .address(mock(Address.class))
                .build();

        UserDetails partners = new PartnersDetails(testPartners);
        UserDetails members = new MemberDetails(testMember);

        return new InMemoryUserDetailsManager(partners, members);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
