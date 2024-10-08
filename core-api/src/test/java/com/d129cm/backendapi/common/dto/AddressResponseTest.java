package com.d129cm.backendapi.common.dto;

import com.d129cm.backendapi.common.domain.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AddressResponseTest {
    @Test
    void AddressResponse반환_Address_팩토리메서드_of() {
        // given
        Address address = new Address("1234", "Seoul", "Seoul");

        // when
        AddressResponse response = AddressResponse.of(address);

        // then
        Assertions.assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.zipCode()).isEqualTo(address.getZipCode()),
                () -> assertThat(response.addressDetails()).isEqualTo(address.getAddressDetails()),
                () -> assertThat(response.roadNameAddress()).isEqualTo(address.getRoadNameAddress())
        );
    }
}
