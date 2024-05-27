package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

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

    @Builder
    public Address(String zipCode, String roadNameAddress, String addressDetails) {
        Assert.notNull(zipCode, "우편번호는 null일 수 없습니다.");
        Assert.notNull(roadNameAddress, "도로명 주소는 null일 수 없습니다.");
        Assert.notNull(addressDetails, "상세 주소는 null일 수 없습니다.");

        this.zipCode = zipCode;
        this.roadNameAddress = roadNameAddress;
        this.addressDetails = addressDetails;
    }
}
