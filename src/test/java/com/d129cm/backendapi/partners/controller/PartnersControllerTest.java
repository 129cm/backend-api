package com.d129cm.backendapi.partners.controller;

import com.d129cm.backendapi.auth.config.PartnersSecurityConfig;
import com.d129cm.backendapi.auth.domain.PartnersUserDetails;
import com.d129cm.backendapi.auth.dto.PartnersLoginRequest;
import com.d129cm.backendapi.auth.service.PartnersUserDetailsService;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.partners.domain.Partners;
import com.d129cm.backendapi.partners.dto.PartnersSignupRequest;
import com.d129cm.backendapi.partners.service.PartnersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnersController.class)
@Import(PartnersSecurityConfig.class)
@SuppressWarnings("NonAsciiCharacters")
public class PartnersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnersUserDetailsService partnersUserDetailsService;

    @MockBean
    private PartnersService partnersService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Nested
    class login {
        @Test
        void 성공_파트너스_로그인_요청() throws Exception {
            // given
            PartnersLoginRequest request = new PartnersLoginRequest("email@email.com", "Asdf123!");
            PartnersUserDetails mockUserDetails = spy(new PartnersUserDetails(mock(Partners.class)));
            when(passwordEncoder.matches(request.password(), mockUserDetails.getPassword())).thenReturn(true);
            when(partnersUserDetailsService.loadUserByUsername(request.email())).thenReturn(mockUserDetails);

            // when
            ResultActions result = mockMvc.perform(post("/partners/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk()).andExpect(content().json("{\"status\":200, \"message\":\"성공\"}"));
        }
    }

    @Nested
    class signup {
        @Test
        void 성공_파트너스_회원가입_요청() throws Exception {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "Asdf123!", "123-12-12312");
            doNothing().when(partnersService).savePartners(request);

            // when
            ResultActions result = mockMvc.perform(post("/partners/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isOk())
                    .andExpect(content().json("{\"status\":200, \"message\":\"성공\"}"));
        }

        @Test
        void status409_이메일_중복() throws Exception {
            // given
            PartnersSignupRequest request = new PartnersSignupRequest("email@naver.com", "Asdf123!", "123-12-12312");
            ConflictException e = new ConflictException("email", request.email());

            doThrow(e).when(partnersService).savePartners(request);

            CommonResponse<Void> expectedResponse = CommonResponse.failure(e.getStatus(), e.getMessage());
            String expectedResponseJson = new ObjectMapper().writeValueAsString(expectedResponse);

            // when
            ResultActions result = mockMvc.perform(post("/partners/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(new ObjectMapper().writeValueAsString(request)));

            // then
            result.andExpect(status().isConflict())
                    .andExpect(content().json(expectedResponseJson));
        }

        static Stream<Arguments> provideInvalidSignupRequests() {
            return Stream.of(
                    arguments(new PartnersSignupRequest("email2naver.com", "Asdf123!", "123-45-67890"),
                            "email: 이메일 형식이 아닙니다."),
                    arguments(new PartnersSignupRequest("email@naver.com", null, "123-45-67890"),
                            "password: 비밀번호는 필수 입력 값입니다."),
                    arguments(new PartnersSignupRequest("email@naver.com", "Asdf123!", null),
                            "businessNumber: 사업자 등록 번호는 필수 입력 값입니다."),
                    arguments(new PartnersSignupRequest("email@naver.com", "123123123", "123-45-67890"),
                            "password: 비밀번호는 영문 대소문자, 숫자를 포함해야 합니다."),
                    arguments(new PartnersSignupRequest("email@naver.com", "asdF12!", "123-45-67890"),
                            "password: 비밀번호는 8자 이상, 20자 이하여야 합니다.")

            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidSignupRequests")
        void status400_유효하지_않은_입력_형식(PartnersSignupRequest inputValue, String errorMessage) throws Exception {
            // given
            CommonResponse<Void> expectedResponse = CommonResponse.failure(HttpStatus.BAD_REQUEST, errorMessage);
            String expectedResponseJson = new ObjectMapper().writeValueAsString(expectedResponse);

            // when
            ResultActions result = mockMvc.perform(post("/partners/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(new ObjectMapper().writeValueAsString(inputValue)));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(content().json(expectedResponseJson));
        }
    }

}