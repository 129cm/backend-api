package com.d129cm.backendapi.partners.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private String description;
    @OneToOne(mappedBy = "brand", fetch = FetchType.LAZY)
    private Partners partners;

    @Builder
    private Brand(String name, String image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
    }
}
