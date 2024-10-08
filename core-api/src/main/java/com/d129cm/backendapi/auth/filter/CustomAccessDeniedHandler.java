package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.common.utils.ServletResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info(accessDeniedException.getMessage());
        CommonResponse<?> errorResponse = CommonResponse.failure(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

        ServletResponseUtil.servletResponse(response, errorResponse);
    }
}
