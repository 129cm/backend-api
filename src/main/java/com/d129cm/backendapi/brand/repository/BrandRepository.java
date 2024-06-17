package com.d129cm.backendapi.brand.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
}
