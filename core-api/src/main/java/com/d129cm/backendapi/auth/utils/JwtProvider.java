package com.d129cm.backendapi.auth.utils;

import com.d129cm.backendapi.auth.domain.Role;
import com.d129cm.backendapi.common.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j(topic = "JwtProvider")
@Component
public class JwtProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.token.expiration.time}")
    public long JWT_TOKEN_EXPIRATION_TIME;
    @Value("${jwt.token.secretKey}")
    private String SECRET_KEY;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, Role role) {
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_TIME))
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String removeBearerPrefix(String token) {
        if (token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw AuthenticationException.unauthenticatedToken(token);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthenticationException.unauthenticatedToken(token);
        }
    }

    public String getSubjectFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return Role.valueOf(claims.get("role", String.class));
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
