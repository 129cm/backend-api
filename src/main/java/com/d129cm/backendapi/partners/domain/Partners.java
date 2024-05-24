package com.d129cm.backendapi.partners.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partners {
    private static final Pattern BUSINESS_NUMBER_PATTERN = Pattern.compile("^(1\\d{2}|[2-9]\\d{2})-\\d{2}-\\d{5}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String businessNumber;
    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Brand brand;

    @Builder
    private Partners(String email, String password, String businessNumber) {
        Assert.notNull(email, "이메일은 null일 수 없습니다.");
        Assert.notNull(password, "비밀번호는 null일 수 없습니다.");
        Assert.notNull(businessNumber, "사업자 번호는 null일 수 없습니다.");
        validateBusinessNumber(businessNumber);

        this.email = email;
        this.password = password;
        this.businessNumber = businessNumber;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        brand.setPartners(this);
    }

    private void validateBusinessNumber(String businessNumber) {
        if (!BUSINESS_NUMBER_PATTERN.matcher(businessNumber).matches()) {
            throw new IllegalArgumentException("사업자 번호 형식이 올바르지 않습니다.");
        }
    }
}
