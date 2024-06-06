package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
