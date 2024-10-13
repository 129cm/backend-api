package com.d129cm.backendapi.member.dto;

import com.d129cm.backendapi.common.dto.AddressResponse;

public record MemberMyPageResponse (
        String email,
        String name,
        AddressResponse address
) {
}
