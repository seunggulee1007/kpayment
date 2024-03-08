package com.kakaoinsurance.payment.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

}
