package com.d129cm.backendapi.partners.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String businessNumber;
    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Brand brand;

    @Builder
    private Partners(String email, String password, String businessNumber, Brand brand) {
        this.email = email;
        this.password = password;
        this.businessNumber = businessNumber;
        this.brand = brand;
    }
}
