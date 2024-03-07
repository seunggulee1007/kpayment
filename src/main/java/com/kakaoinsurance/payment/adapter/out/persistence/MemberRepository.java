package com.kakaoinsurance.payment.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemeberEntity, Long> {

    Optional<MemeberEntity> findByEmail(String email);

}
