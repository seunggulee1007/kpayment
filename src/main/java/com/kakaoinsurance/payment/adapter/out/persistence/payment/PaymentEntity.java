package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
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

import static com.kakaoinsurance.payment.common.utils.CommonUtil.calculateTax;

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

    /**
     * 결제 금액
     */
    @Column(nullable = false, length = 10)
    private double price;

    /**
     * 부가세
     */
    @Column(length = 10)
    private Long tax;

    /**
     * 잔여 결제 금액
     * ( 원 거래 테이블에서만 관리 )
     */
    private double remainPrice;

    /**
     * 잔여 부가세
     * ( 원 거래 테이블에서만 관리 )
     */
    private Long remainTax;

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
        entity.remainPrice = payment.price();
        entity.remainTax = payment.tax();
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

    /**
     * 전체 취소 요청 용 객체 새로 만드는 함수
     *
     * @param payment 취소 요청 용 객체
     * @return 취소 요청용 결재 객체
     */
    public PaymentEntity cancel(Payment payment) {
        validatePrice(payment.price());
        Long cancelTax = calculateTax(payment.tax(), payment.price());
        if (!cancelTax.equals(this.tax)) {
            throw new PaymentBadRequestException("전체 취소일 경우 부가세 금액이 일치해야 합니다.");
        }
        PaymentEntity entity = new PaymentEntity();
        entity.parent = this;
        entity.paymentId = PaymentIdGenerator.generate();
        entity.cardData = this.cardData;
        entity.paymentKind = PaymentKind.CANCEL;
        entity.installmentMonth = InstallmentMonth.ZERO;
        entity.price = payment.price();
        entity.tax = cancelTax;

        this.remainPrice -= payment.price();
        this.remainTax -= cancelTax;
        this.cancelList.add(entity);

        return entity;
    }

    private void validatePrice(double cancelPrice) {
        if (this.remainPrice < this.price) {
            throw new PaymentBadRequestException("결제에 대한 전체취소는 1번만 가능합니다.");
        }
        if (this.price != cancelPrice) {
            throw new PaymentBadRequestException("전체 취소일 경우 결제 금액과 취소 금액이 일치해야 합니다.");
        }
    }

}
