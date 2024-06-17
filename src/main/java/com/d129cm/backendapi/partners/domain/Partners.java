package com.d129cm.backendapi.partners.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.Password;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private Password password;
    @Column(unique = true, nullable = false)
    private String businessNumber;
    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Brand brand;

    @Builder
    private Partners(String email, Password password, String businessNumber) {
        Assert.notNull(email, "이메일은 null일 수 없습니다.");
        Assert.notNull(password, "비밀번호는 null일 수 없습니다.");
        Assert.notNull(businessNumber, "사업자 번호는 null일 수 없습니다.");

        this.email = email;
        this.password = password;
        this.businessNumber = businessNumber;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        brand.setPartners(this);
    }
}
