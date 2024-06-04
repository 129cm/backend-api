package com.d129cm.backendapi.auth.filter;

import com.d129cm.backendapi.auth.domain.Role;
import com.d129cm.backendapi.auth.utils.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService detailsService;
    private final RequestMatcher matchers;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (matchers.matches(req)) {
            filterChain.doFilter(req, res);
            return;
        }

        try {
            String tokenValue = jwtProvider.removeBearerPrefix(req.getHeader(HttpHeaders.AUTHORIZATION));
            jwtProvider.validateToken(tokenValue);
            String username = jwtProvider.getSubjectFromToken(tokenValue);
            UserDetails userDetails = detailsService.loadUserByUsername(username);

            Role role = jwtProvider.getRoleFromToken(tokenValue);
            if (!userDetails.getAuthorities().contains(role))
                throw new AccessDeniedException("Invalid Role");

            setAuthentication(username, role);
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            filterChain.doFilter(req, res);
        }
    }

    private void setAuthentication(String username, Role role) {
        Authentication authentication = createAuthentication(username, role);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(String username, Role role) {
        UserDetails userDetails = detailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(role));
    }
}
