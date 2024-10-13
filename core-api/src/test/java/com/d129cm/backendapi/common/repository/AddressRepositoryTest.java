package com.d129cm.backendapi.common.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.common.domain.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @Test
    void 성공_주소_저장() {
        //given
        Address address = Address.builder()
                .zipCode("1234")
                .addressDetails("주소시 주소구 주소로")
                .roadNameAddress("101동 101호")
                .build();

        // when
        Address savedAddress = addressRepository.save(address);

        // then
        Assertions.assertAll(
                () -> assertThat(savedAddress).isNotNull(),
                () -> assertThat(savedAddress.getId()).isEqualTo(address.getId()),
                () -> assertThat(savedAddress.getZipCode()).isEqualTo(address.getZipCode()),
                () -> assertThat(savedAddress.getAddressDetails()).isEqualTo(address.getAddressDetails()),
                () -> assertThat(savedAddress.getRoadNameAddress()).isEqualTo(address.getRoadNameAddress())
        );
    }
}
