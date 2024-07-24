package com.d129cm.backendapi.item.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer optionPrice = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    @Builder
    private ItemOption(String name, Integer quantity, Integer optionPrice) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(quantity, "수량은 null일 수 없습니다.");
        Assert.notNull(optionPrice, "옵션 가격은 null일 수 없습니다.");

        this.name = name;
        this.quantity = quantity;
        this.optionPrice = optionPrice;
    }

    public void updateItem(Item item) {
        Assert.notNull(item, "item은 null일 수 없습니다.");
        this.item = item;
    }
}