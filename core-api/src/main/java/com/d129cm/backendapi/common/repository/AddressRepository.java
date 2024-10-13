package com.d129cm.backendapi.common.repository;

import com.d129cm.backendapi.common.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
