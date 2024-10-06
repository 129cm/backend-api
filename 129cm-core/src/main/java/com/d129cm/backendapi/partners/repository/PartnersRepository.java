package com.d129cm.backendapi.partners.repository;

import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnersRepository extends JpaRepository<Partners, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "brand", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Partners> findByEmail(String email);
}
