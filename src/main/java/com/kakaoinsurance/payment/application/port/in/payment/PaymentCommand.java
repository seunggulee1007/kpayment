package com.kakaoinsurance.payment.application.port.in.payment;

import com.kakaoinsurance.payment.common.SelfValidating;
import com.kakaoinsurance.payment.common.advice.exceptions.PaymentBadRequestException;
import com.kakaoinsurance.payment.domain.payment.InstallmentMonth;
import com.kakaoinsurance.payment.domain.payment.Payment;
import com.kakaoinsurance.payment.domain.payment.PaymentKind;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

import static com.kakaoinsurance.payment.common.DefaultMessage.*;
import static com.kakaoinsurance.payment.common.utils.CommonUtil.calculateTax;

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
    @NotNull(message = "결제금액은 필수입니다.")
    private Double price;

    /**
     * 부가 가치세 ( optional )
     */
    private Long tax;

    public Payment mapToDomain(String cardData) {
        this.tax = calculateTax(this.tax, this.price);
        return Payment.builder()
            .cardData(cardData)
            .paymentKind(PaymentKind.PAYMENT)
            .installmentMonth(this.installmentMonth)
            .price(this.price)
            .tax(this.tax)
            .build();
    }

    public void validate() {
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
        if (isWithinRangePrice()) {
            throw new PaymentBadRequestException(PAYMENT_RANGE.getMessage());
        }
        if (isRightTax()) {
            throw new PaymentBadRequestException(CANCEL_TAX_LATHER_THEN_PRICE.getMessage());
        }
    }

    private boolean isRightTax() {
        return this.tax != null && this.tax > this.price;
    }

    private boolean isWithinRangePrice() {
        return this.price < 100 || this.price > 1000000000;
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
        throw new PaymentBadRequestException(NOT_VALID_YMD.getMessage());
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
        String yearPrefix = year.substring(0, 2);
        int yy = Integer.parseInt(yearPrefix.concat(yyString));
        LocalDate firstDayOfValidYmd = LocalDate.of(yy, mm, 1);
        return firstDayOfValidYmd.withDayOfMonth(firstDayOfValidYmd.lengthOfMonth());
    }

    public String getCardData(String separator) {
        return this.cardNumber.concat(separator).concat(this.validYmd).concat(separator).concat(this.cvc);
    }

}
