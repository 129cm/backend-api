package com.d129cm.backendapi.member.dto;

public record ItemResponse(
        Long id,
        String name,
        Integer price,
        String image
) {
}

