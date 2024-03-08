package com.kakaoinsurance.payment.application.port.in;

import com.kakaoinsurance.payment.common.SelfValidating;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.domain.InstallmentMonth;
import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.domain.PaymentKind;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentCommand extends SelfValidating<PaymentCommand> {

    /**
     * 카드 번호 ( 10 ~ 16 자리 숫자 )
     */
    @NotNull(message = "카드 번호는 필수 입니다.")
    @Size(min = 10, max = 16, message = "카드 번호는 10 ~ 16자리만 가능합니다.")
    private String cardNumber;

    /**
     * 유효기간 ( 4자리 숫자 , mmyy )
     */
    @NotNull(message = "유효기간은 필수 입니다.")
    private String validYmd;

    /**
     * cvc (3자리 숫자 )
     */
    @NotNull(message = "cvc 는 필수 입니다.")
    private String cvc;

    /**
     * 할부개월수: 0(일시불), 1 ~ 12
     */
    @NotNull(message = "할부 개월수를 선택해 주세요.")
    private InstallmentMonth installmentMonth;

    /**
     * 결제금액(100원 이상, 10억원 이하, 숫자)
     */
    private double price;

    /**
     * 부가 가치세 ( optional )
     */
    private Long tax;

    public Payment mapToDomain(String cardData) {
        validate();
        calculateTax();
        return Payment.builder()
            .cardData(cardData)
            .paymentKind(PaymentKind.PAYMENT)
            .installmentMonth(this.installmentMonth)
            .price(this.price)
            .tax(this.tax)
            .build();
    }

    private void validate() {
        this.validateSelf();
        this.validateValidYmd();
        this.validateCvc();
        this.validatePrice();
    }

    private void validateCvc() {
        if (cvc.length() != 3) {
            throw new PaymentBadRequestException("cvc가 바람직하지 않습니다.");
        }
    }

    private void validatePrice() {
        if (this.price < 100 || this.price > 1000000000) {
            throw new PaymentBadRequestException("결제금액은 100원 이상 10억원 이하 입니다.");
        }
        if (this.tax != null && this.tax > this.price) {
            throw new PaymentBadRequestException("부가 가치세가 결제금액보다 클 수 없습니다.");
        }
    }

    /**
     * 유효기간 검증
     */
    private void validateValidYmd() {
        if (validYmd.length() != 4) {
            throwValidYmdException();
        }
        int mm = Integer.parseInt(validYmd.substring(0, 2));
        if (mm < 1 || mm > 12) {
            throwValidYmdException();
        }
        LocalDate now = LocalDate.now();
        LocalDate validYmdLocalDate = getValidYmdLocalDate(mm, Integer.toString(now.getYear()));
        if (validYmdLocalDate.isBefore(now)) {
            throwValidYmdException();
        }

    }

    public void throwValidYmdException() {
        throw new PaymentBadRequestException("유효기간이 바람직하지 않습니다.");
    }

    /**
     * 유효기간의 마지막 날 LocalDate
     * 현재일자가 같은 달일수도 있기 때문에 마지막 날 기준으로 보기 위해 마지막 날을 가져온다.
     *
     * @param mm   월
     * @param year 현재 년도
     * @return 유효기간의 마지막 날
     */
    private LocalDate getValidYmdLocalDate(int mm, String year) {
        String yyString = validYmd.substring(2, 4);
        String yearPrefix = year.substring(2, 4);
        int yy = Integer.parseInt(yearPrefix.concat(yyString));
        LocalDate firstDayOfValidYmd = LocalDate.of(yy, mm, 1);
        return firstDayOfValidYmd.withDayOfMonth(firstDayOfValidYmd.lengthOfMonth());
    }

    /**
     * 부가 가치세를 계산한다.
     * <p>자동계산 수식 : 결제금액 / 11, 소수점이하 반올림</p>
     * <p>결제금액이 1,000원일 경우, 자동계산된 부가가치세는 91원입니다.</p>
     */
    private void calculateTax() {
        if (this.tax == null) {
            this.tax = Math.round(this.price / 11);
        }
    }

    public String getCardData(String separator) {
        return this.cardNumber.concat(separator).concat(this.validYmd).concat(separator).concat(this.cvc);
    }

}
