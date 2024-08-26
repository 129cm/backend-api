package com.d129cm.backendapi.member.repository;

import com.d129cm.backendapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    @Query(value = "select m from Member m join Address a on m.id = a.member.id")
    Optional<Member> findByEmailWithAddress(String email);
}
