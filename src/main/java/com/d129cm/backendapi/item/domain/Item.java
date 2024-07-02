package com.d129cm.backendapi.item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private Integer price = 0;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ItemOption> itemOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private Brand brand;

    @Builder
    public Item(String name, Integer price, String image, String description, List<ItemOption> itemOptions) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(price, "가격은 null일 수 없습니다.");
        Assert.notNull(image, "이미지는 null일 수 없습니다.");
        Assert.notNull(description, "설명은 null일 수 없습니다.");
        if (price < 0) throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        if (itemOptions == null || itemOptions.isEmpty())
            throw new IllegalArgumentException("아이템 옵션은 null일 수 없습니다.");

        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.itemOptions = itemOptions;
    }

    public void addItemOption(ItemOption itemOption) {
        Assert.notNull(itemOption, "itemOption은 null일 수 없습니다.");
        itemOptions.add(itemOption);
    }

    public void updateBrand(Brand brand) {
        Assert.notNull(brand, "brand는 null일 수 없습니다.");
        this.brand = brand;
        brand.addItem(this);
    }
}