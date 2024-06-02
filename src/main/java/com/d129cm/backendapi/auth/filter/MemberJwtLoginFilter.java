package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.domain.Role;
import com.d129cm.backendapi.auth.dto.LoginRequest;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.global.utils.ServletResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MemberJwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public MemberJwtLoginFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/members/login");
        setPostOnly(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();
        Role role = userDetails.getAuthorities().stream().findFirst().map(Role.class::cast).orElseThrow();

        String token = jwtProvider.createToken(username, role);

        response.addHeader(HttpHeaders.AUTHORIZATION, token);

        CommonResponse<?> successResponse = CommonResponse.success(HttpStatus.OK, null);
        ServletResponseUtil.servletResponse(response, successResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CommonResponse<?> errorResponse = CommonResponse.failure(HttpStatus.UNAUTHORIZED, "인증 과정 중 오류 발생");

        ServletResponseUtil.servletResponse(response, errorResponse);
    }
}
