package com.d129cm.backendapi.auth.dto;

public record LoginRequest (
        String email,
        String password
){
}
