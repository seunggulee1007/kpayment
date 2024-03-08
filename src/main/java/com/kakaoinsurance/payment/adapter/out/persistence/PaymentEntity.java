package com.kakaoinsurance.payment.adapter.out.persistence;

import com.kakaoinsurance.payment.domain.InstallmentMonth;
import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.domain.PaymentId;
import com.kakaoinsurance.payment.domain.PaymentKind;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 결제 식별자
     */
    @Column(unique = true, length = 20)
    private String paymentId;

    /**
     * 카드 데이터
     */
    @Column(nullable = false, length = 300)
    private String cardData;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private PaymentKind paymentKind;

    @Column(nullable = false, length = 2)
    @Convert(converter = InstallmentMonthConverter.class)
    private InstallmentMonth installmentMonth;

    /**
     * 원 관리번호
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PaymentEntity parent;

    /**
     * 결제 취소 정보
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<PaymentEntity> cancelList = new ArrayList<>();

    @Column(nullable = false, length = 10)
    private double price;

    @Column(length = 10)
    private double tax;

    @CreatedBy
    private String paidBy;

    @CreatedDate
    private LocalDateTime paidAt;

    public static PaymentEntity mapToEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.paymentId = PaymentIdGenerator.generate();
        entity.cardData = payment.cardData();
        entity.paymentKind = payment.paymentKind();
        entity.installmentMonth = payment.installmentMonth();
        entity.price = payment.price();
        entity.tax = payment.tax();
        return entity;
    }

    public Payment mapToDomain() {
        return Payment.builder()
            .paymentId(PaymentId.of(this.paymentId))
            .cardData(this.cardData)
            .paymentKind(this.paymentKind)
            .installmentMonth(this.installmentMonth)
            .price(this.price)
            .tax(this.tax)
            .originalManagementId(this.parent == null ? null : this.parent.paymentId)
            .paidBy(this.paidBy)
            .paidAt(this.paidAt)
            .build();
    }

}
