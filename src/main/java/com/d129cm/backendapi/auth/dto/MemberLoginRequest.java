package com.d129cm.backendapi.auth.dto;

public record MemberLoginRequest(
        String email,
        String password
){
}
