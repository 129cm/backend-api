package com.d129cm.backendapi.fixture;

import com.d129cm.backendapi.common.domain.Address;

import static org.mockito.Mockito.spy;

public class AddressFixture {
    private AddressFixture() {}
    public static Address createAddress() {
        Address address = spy(Address.builder()
                .roadNameAddress("서울시")
                .addressDetails("서울로")
                .zipCode("123-12")
                .build());
        return address;
    }
}
