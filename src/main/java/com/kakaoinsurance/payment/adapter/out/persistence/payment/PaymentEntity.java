package com.kakaoinsurance.payment.adapter.out.persistence.payment;

import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import com.kakaoinsurance.payment.domain.payment.PaymentId;
import com.kakaoinsurance.payment.domain.payment.PaymentKind;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kakaoinsurance.payment.common.DefaultMessage.*;
import static com.kakaoinsurance.payment.common.utils.CommonUtil.calculateTax;

@Getter
@Entity
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PaymentEntity {

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
            .remainPrice(this.remainPrice)
            .remainTax(this.remainTax)
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
        double calculatePrice = this.remainPrice - payment.price();
        Long cancelTax = calculateCancelTax(payment, calculatePrice);
        if (!cancelTax.equals(this.tax)) {
            throw new PaymentBadRequestException(NOT_VALID_TAX.getMessage());
        }
        this.remainPrice -= payment.price();
        this.remainTax -= cancelTax;
        return this.getCancelEntity(payment, cancelTax);
    }

    private void validatePrice(double cancelPrice) {
        if (this.remainPrice < this.price) {
            throw new PaymentBadRequestException(ALL_CANCEL_AVAILABLE.getMessage());
        }
        if (this.price != cancelPrice) {
            throw new PaymentBadRequestException(NOT_MATCHED_CANCEL_PRICE.getMessage());
        }
    }

    public PaymentEntity partCancel(Payment payment) {
        if (this.remainPrice == 0) {
            throw new PaymentBadRequestException(COMPLETED_CANCEL.getMessage());
        }
        Double cancelPrice = payment.price();
        if (cancelPrice > this.remainPrice) {
            throw new PaymentBadRequestException(CANCEL_PRICE_LATHER_THEN_PRICE.getMessage());
        }
        double calculatePrice = this.remainPrice - payment.price();
        Long cancelTax = this.calculateCancelTax(payment, calculatePrice);
        if (cancelTax > this.remainTax) {
            throw new PaymentBadRequestException(NOT_VALID_TAX.getMessage());
        }
        this.remainPrice = calculatePrice;
        this.remainTax -= cancelTax;
        return this.getCancelEntity(payment, cancelTax);
    }

    private Long calculateCancelTax(Payment payment, double calculatePrice) {
        Long cancelTax = payment.tax();
        if (calculatePrice == 0) {
            if (cancelTax == null) {
                return this.remainTax;
            } else if (!cancelTax.equals(this.remainTax)) {
                throw new PaymentBadRequestException(NOT_VALID_TAX.getMessage());
            }
            return cancelTax;
        }
        return calculateTax(payment.tax(), payment.price());
    }

    private PaymentEntity getCancelEntity(Payment payment, Long cancelTax) {
        PaymentEntity entity = new PaymentEntity();
        entity.parent = this;
        entity.paymentId = PaymentIdGenerator.generate();
        entity.cardData = this.cardData;
        entity.paymentKind = PaymentKind.CANCEL;
        entity.installmentMonth = InstallmentMonth.ZERO;
        entity.price = payment.price();
        entity.tax = cancelTax;

        this.cancelList.add(entity);
        return entity;
    }

}
