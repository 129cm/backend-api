package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByBrandId(Long brandId, Pageable pageable);
}
