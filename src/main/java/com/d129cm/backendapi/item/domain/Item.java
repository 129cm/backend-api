package com.d129cm.backendapi.item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "update item set deleted = true where id = ?")
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

    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ItemOption> itemOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Brand brand;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false, updatable = false)
    private final boolean deleted = false;

    @Builder
    public Item(String name, Integer price, String image, String description) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(price, "가격은 null일 수 없습니다.");
        Assert.notNull(image, "이미지는 null일 수 없습니다.");
        Assert.notNull(description, "설명은 null일 수 없습니다.");
        if (price < 0) throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");

        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public void addItemOption(ItemOption itemOption) {
        Assert.notNull(itemOption, "itemOption은 null일 수 없습니다.");
        itemOptions.add(itemOption);
        itemOption.updateItem(this);
    }

    public void updateBrand(Brand brand) {
        Assert.notNull(brand, "brand는 null일 수 없습니다.");
        this.brand = brand;
    }

    public void updateItem(Item item) {
        Assert.notNull(item, "Item은 null일 수 없습니다.");
        this.name = item.getName();
        this.price = item.getPrice();
        this.image = item.getImage();
        this.description = item.getDescription();
        updateItemOptions(item.getItemOptions());
    }

    private void updateItemOptions(List<ItemOption> newOptions) {
        this.itemOptions.removeIf(existingOption ->
                newOptions.stream().noneMatch(newOption -> newOption.getId().equals(existingOption.getId()))
        );

        for (ItemOption newOption : newOptions) {
            ItemOption existingOption = this.itemOptions.stream()
                    .filter(option -> option.getId().equals(newOption.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingOption == null) {
                this.addItemOption(newOption);
            } else {
                existingOption.update(newOption);
            }
        }
    }
}