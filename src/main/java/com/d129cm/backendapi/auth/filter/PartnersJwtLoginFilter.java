package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.dto.PartnersLoginRequest;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.common.utils.ServletResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PartnersJwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PARTNERS_LOGIN_URL = "/partners/login";

    public PartnersJwtLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl(PARTNERS_LOGIN_URL);
        setPostOnly(true);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            PartnersLoginRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), PartnersLoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(),
                            requestDto.password()
                    )
            );
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 시도 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        CommonResponse<?> successResponse = CommonResponse.success();
        String jsonResponse = new ObjectMapper().writeValueAsString(successResponse);

        response.setStatus(successResponse.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        CommonResponse<?> errorResponse = CommonResponse.failure(HttpStatus.UNAUTHORIZED, "인증 과정 중 오류 발생");

        ServletResponseUtil.servletResponse(response, errorResponse);
    }
}
