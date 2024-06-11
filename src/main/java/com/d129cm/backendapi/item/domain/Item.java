package com.d129cm.backendapi.item.domain;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<ItemOption> itemOptions;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Builder
    public Item(String name, Integer price, Integer quantity, String image, String description) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(price, "가격은 null일 수 없습니다.");
        Assert.notNull(quantity, "수량은 null일 수 없습니다.");
        Assert.notNull(image, "이미지는 null일 수 없습니다.");
        Assert.notNull(description, "설명은 null일 수 없습니다.");

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
    }

}
