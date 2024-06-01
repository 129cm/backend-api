package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.domain.Role;
import com.d129cm.backendapi.auth.service.MemberDetailsService;
import com.d129cm.backendapi.auth.utils.CookieUtils;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import com.d129cm.backendapi.common.dto.CommonResponse;
import com.d129cm.backendapi.global.utils.ServletResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

     private final JwtProvider jwtProvider;
     private final MemberDetailsService memberService;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if(req.getRequestURI().equals("/members/signup")) {
            filterChain.doFilter(req, res);
            return;
        }

        try {
            CookieUtils.getCookie(req, HttpHeaders.AUTHORIZATION).ifPresent(
                    cookie -> {
                        String tokenValue = cookie.getValue();
                        tokenValue = jwtProvider.removeBearerPrefix(tokenValue);
                        jwtProvider.validateToken(tokenValue);
                        String username = jwtProvider.getSubjectFromToken(tokenValue);
                        Role role = jwtProvider.getRoleFromToken(tokenValue);
                        setAuthentication(username, role);
                    }
            );
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();

            CommonResponse<?> responseDto = CommonResponse.failure(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            ServletResponseUtil.servletResponse(res, responseDto);
        }
    }

    private void setAuthentication(String username, Role role) {
        Authentication authentication = createAuthentication(username, role);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(String username, Role role) {
        UserDetails userDetails = memberService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(role));
    }
}
