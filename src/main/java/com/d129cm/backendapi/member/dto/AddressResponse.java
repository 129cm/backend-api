package com.d129cm.backendapi.member.dto;

public record AddressResponse (
        String zipCode,
        String roadNameAddress,
        String addressDetails
) {
}
