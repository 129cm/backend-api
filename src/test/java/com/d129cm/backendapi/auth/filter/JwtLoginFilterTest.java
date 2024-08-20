package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.config.MemberSecurityConfig;
import com.d129cm.backendapi.auth.domain.MemberDetails;
import com.d129cm.backendapi.auth.dto.MemberLoginRequest;
import com.d129cm.backendapi.auth.service.MemberDetailsService;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;
import com.d129cm.backendapi.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JwtLoginFilter.class)
@ContextConfiguration(classes = {MemberSecurityConfig.class})
class JwtLoginFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MemberDetailsService memberDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Nested
    class Authenticate {
        @Test
        void 성공200반환_멤버_로그인_성공() throws Exception {
            // given
            MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test@naver.com", "Asdf1234!");
            String loginRequestJson = new ObjectMapper().writeValueAsString(memberLoginRequest);

            Member member = new Member("test@naver.com", Password.of("Asdf1234", passwordEncoder), "이름", mock(Address.class));
            MemberDetails memberDetails = new MemberDetails(member);

            Authentication authResult = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            when(authenticationManager.authenticate(any())).thenReturn(authResult);
            when(jwtProvider.createToken(any(), any())).thenReturn("mocked-jwt-token");

            // when
            ResultActions perform = mockMvc.perform(post("/members/login")
                    .contentType("application/json")
                    .content(loginRequestJson));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(header().string("Authorization", "mocked-jwt-token"));
        }

        @Test
        void 에러403반환_멤버_로그인_실패() throws Exception {
            // given
            MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test@naver.com", "wrongPassword");
            String loginRequestJson = new ObjectMapper().writeValueAsString(memberLoginRequest);

            when(authenticationManager.authenticate(any()))
                    .thenThrow(new AuthenticationServiceException("Invalid credentials"));

            // when
            ResultActions perform = mockMvc.perform(post("/members/login")
                    .contentType("application/json")
                    .content(loginRequestJson));

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 과정 중 오류 발생"));
        }
    }
}