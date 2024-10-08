package com.d129cm.backendapi.common.dto;

import com.d129cm.backendapi.common.domain.Address;

public record AddressResponse (
        String zipCode,
        String roadNameAddress,
        String addressDetails
) {
    public static AddressResponse of (Address address) {
        return new AddressResponse(
                address.getZipCode(),
                address.getRoadNameAddress(),
                address.getAddressDetails()
        );
    }
}
