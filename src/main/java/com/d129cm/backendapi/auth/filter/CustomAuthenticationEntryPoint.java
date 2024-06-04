package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.common.utils.ServletResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info(authException.getMessage());
        CommonResponse<?> errorResponse = CommonResponse.failure(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");

        ServletResponseUtil.servletResponse(response, errorResponse);
    }
}
