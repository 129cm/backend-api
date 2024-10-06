package com.d129cm.backendapi.member.domain;

import com.d129cm.backendapi.cart.domain.Cart;
import com.d129cm.backendapi.common.domain.Address;
import com.d129cm.backendapi.common.domain.Password;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded
    private Password password;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Cart cart;

    private String membershipLevelCode;

    @Builder
    public Member (String email, Password password, String name, Address address) {
        Assert.notNull(email, "이메일은 null일 수 없습니다.");
        Assert.notNull(password, "비밀번호는 null일 수 없습니다.");
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(address, "주소는 null일 수 없습니다.");

        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        address.updateMember(this);
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        cart.setMember(this);
    }

    public void updateMembershipLevel(String name) {
        this.membershipLevelCode = name;
    }
}
