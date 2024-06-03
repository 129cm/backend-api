package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.config.PartnersSecurityConfig;
import com.d129cm.backendapi.auth.domain.PartnersDetails;
import com.d129cm.backendapi.auth.dto.PartnersLoginRequest;
import com.d129cm.backendapi.auth.service.PartnersDetailsService;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import com.d129cm.backendapi.partners.domain.Partners;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnersJwtLoginFilter.class)
@ContextConfiguration(classes = {PartnersSecurityConfig.class})
@SuppressWarnings("NonAsciiCharacters")
public class PartnersJwtLoginFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PartnersDetailsService partnersDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Nested
    class Authenticate {
        @Test
        void 성공200반환_파트너스_로그인_성공() throws Exception {
            // given
            PartnersLoginRequest partnersLoginRequest = new PartnersLoginRequest("test@naver.com", "Asdf1234!");
            String loginRequestJson = new ObjectMapper().writeValueAsString(partnersLoginRequest);

            PartnersDetails partnersDetails = spy(new PartnersDetails(mock(Partners.class)));

            Authentication authResult = new UsernamePasswordAuthenticationToken(partnersDetails, null, partnersDetails.getAuthorities());
            when(partnersDetailsService.loadUserByUsername(partnersLoginRequest.email())).thenReturn(partnersDetails);
            when(authenticationManager.authenticate(any())).thenReturn(authResult);
            when(jwtProvider.createToken(any(), any())).thenReturn("mocked-jwt-token");

            // when
            ResultActions result = mockMvc.perform(post("/partners/login")
                    .contentType("application/json")
                    .content(loginRequestJson));


            // then
            result.andExpect(status().isOk())
                    .andExpect(header().string("Authorization", "mocked-jwt-token"));
        }

        @Test
        void 에러403반환_파트너스_로그인_실패() throws Exception {
            // given
            PartnersLoginRequest partnersLoginRequest = new PartnersLoginRequest("test@naver.com", "wrong-password");
            String loginRequestJson = new ObjectMapper().writeValueAsString(partnersLoginRequest);

            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // when
            ResultActions perform = mockMvc.perform(post("/partners/login")
                    .contentType("application/json")
                    .content(loginRequestJson));

            // then
            perform.andExpect(status().isUnauthorized());
        }
    }
}
