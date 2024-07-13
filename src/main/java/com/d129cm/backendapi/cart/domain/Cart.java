package com.d129cm.backendapi.cart.domain;

import com.d129cm.backendapi.member.domain.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    private Member member;

}
