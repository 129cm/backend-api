package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.item.domain.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
}
