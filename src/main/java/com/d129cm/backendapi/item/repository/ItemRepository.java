package com.d129cm.backendapi.item.repository;

import com.d129cm.backendapi.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByBrandId(Long brandId, Pageable pageable);

    @Query(value = "select i from Item i where i.brand.id in (select b.id from Brand b join Partners p on p.brand.id = b.id where p.id = :partnersId)",
            countQuery = "select count(i) from Item i where i.brand.id in (select b.id from Brand b join Partners p on p.brand.id = b.id where p.id = :partnersId)")
    Page<Item> findAllByPartnersId(@Param("partnersId") Long partnersId, Pageable pageable);
}
