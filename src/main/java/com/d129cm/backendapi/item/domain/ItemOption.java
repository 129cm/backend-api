package com.d129cm.backendapi.item.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLDelete(sql = "update item_option set deleted = true where id = ?")
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Item item;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false, updatable = false)
    private boolean deleted;

    @Builder
    private ItemOption(Long id, String name, Integer quantity, Integer optionPrice) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(quantity, "수량은 null일 수 없습니다.");
        Assert.notNull(optionPrice, "옵션 가격은 null일 수 없습니다.");

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.optionPrice = optionPrice;
    }

    public void updateItem(Item item) {
        Assert.notNull(item, "item은 null일 수 없습니다.");
        this.item = item;
    }

    public void update(ItemOption option) {
        Assert.notNull(option, "itemOption은 null일 수 없습니다.");
        this.name = option.getName();
        this.quantity = option.getQuantity();
        this.optionPrice = option.getOptionPrice();
    }
}