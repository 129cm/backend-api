package com.d129cm.backendapi.common.dto;

import com.d129cm.backendapi.common.domain.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest (
        @NotBlank(message = "우편번호는 필수 입력 값입니다.")
        String zipCode,

        @NotBlank(message = "도로명 주소는 필수 입력 값입니다.")
        String roadNameAddress,

        @NotBlank(message = "상세 주소는 필수 입력 값입니다.")
        String addressDetails
) {
    public Address toAddressEntity() {
        return Address.builder()
                .zipCode(zipCode)
                .roadNameAddress(roadNameAddress)
                .addressDetails(addressDetails)
                .build();
    }
}