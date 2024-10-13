package com.d129cm.batchapi.batch;

public record MemberDto(
        Long id,
        String name,
        String email,
        String membershipLevelCode) {
}
