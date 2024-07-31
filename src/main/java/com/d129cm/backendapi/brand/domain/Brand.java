package com.d129cm.backendapi.brand.domain;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.partners.domain.Partners;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String description;
    @Setter
    @OneToOne(mappedBy = "brand", fetch = FetchType.LAZY)
    private Partners partners;
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Item> items = new ArrayList<>();

    @Builder
    private Brand(String name, Partners partners, String image, String description) {
        Assert.notNull(name, "이름은 null일 수 없습니다.");
        Assert.notNull(image, "이미지는 null일 수 없습니다.");
        Assert.notNull(description, "설명은 null일 수 없습니다.");

        this.partners = partners;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public void addItem(Item item) {
        Assert.notNull(item, "item은 null일 수 없습니다.");
        items.add(item);
        item.updateBrand(this);
    }
}
