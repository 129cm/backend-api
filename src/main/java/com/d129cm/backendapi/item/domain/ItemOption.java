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
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private ItemOption(String name, Integer quantity) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(quantity, "수량은 null일 수 없습니다.");

        this.name = name;
        this.quantity = quantity;
    }
}
