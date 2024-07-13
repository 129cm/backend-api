package com.d129cm.backendapi.cart.domain;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.domain.ItemOption;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ItemCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private Integer count;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ItemOption itemOption;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Cart cart;

    public ItemCart(Integer count, Item item, ItemOption itemOption, Cart cart) {
        Assert.notNull(count, "아이템 수량이 null일 수 없습니다.");
        Assert.notNull(cart, "장바구니가 null일 수 없습니다.");

        this.count = count;
        this.item = item;
        this.itemOption = itemOption;
        this.cart = cart;
    }

}
