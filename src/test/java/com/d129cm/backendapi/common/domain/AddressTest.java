package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AddressTest {
    @Nested
    class create {
        @Test
        void 생성성공_주어진_필드로_주소_생성() {
            // given
            String zipCode = "1234";
            String roadNameAddress = "주소시 주소구 주소로";
            String addressDetails = "101동 101호";
            Member member = Mockito.mock(Member.class);

            // when
            Address address = Address.builder()
                    .zipCode(zipCode)
                    .roadNameAddress(roadNameAddress)
                    .addressDetails(addressDetails)
                    .member(member)
                    .build();

            // then
            Assertions.assertAll(
                    () -> Assertions.assertNull(address.getId()),
                    () -> Assertions.assertEquals(address.getZipCode(), zipCode),
                    () -> Assertions.assertEquals(address.getRoadNameAddress(), roadNameAddress),
                    () -> Assertions.assertEquals(address.getAddressDetails(), addressDetails),
                    () -> Assertions.assertEquals(address.getMember(), member)
            );
        }

        @Test
        void 예외발생_주소_필드가_Null일_때() {
            // given
            String zipCode = "1234";
            String roadNameAddress = "주소시 주소구 주소로";
            String addressDetails = "101동 101호";
            Member member = Mockito.mock(Member.class);

            // when & then
            Assertions.assertAll(
                    () -> assertThrowsNullPointerException(null, roadNameAddress, addressDetails, member),
                    () -> assertThrowsNullPointerException(zipCode, null, addressDetails, member),
                    () -> assertThrowsNullPointerException(zipCode, roadNameAddress, null, member),
                    () -> assertThrowsNullPointerException(zipCode, roadNameAddress, addressDetails, null)
            );
        }

        private void assertThrowsNullPointerException(String zipCode, String roadNameAddress, String addressDetails, Member member) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Address.builder()
                    .zipCode(zipCode)
                    .roadNameAddress(roadNameAddress)
                    .addressDetails(addressDetails)
                    .member(member)
                    .build());
        }
    }
}
