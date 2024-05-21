package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipCode;

    private String roadNameAddress;

    private String addressDetails;

    @OneToOne(mappedBy = "address")
    private Member member;

    public Address(String zipCode, String roadNameAddress, String addressDetails, Member member) {
        this.zipCode = zipCode;
        this.roadNameAddress = roadNameAddress;
        this.addressDetails = addressDetails;
        this.member = member;
    }
}
