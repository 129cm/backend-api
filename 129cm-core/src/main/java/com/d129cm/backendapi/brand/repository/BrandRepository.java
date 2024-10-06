package com.d129cm.backendapi.brand.repository;

import com.d129cm.backendapi.brand.domain.Brand;
import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);

    @EntityGraph(attributePaths = {"items"})
    Optional<Brand> findByPartners(Partners partners);
}
