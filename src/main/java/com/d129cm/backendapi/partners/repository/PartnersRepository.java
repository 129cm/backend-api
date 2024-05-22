package com.d129cm.backendapi.partners.repository;

import com.d129cm.backendapi.partners.domain.Partners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnersRepository extends JpaRepository<Partners, Long>{
}
