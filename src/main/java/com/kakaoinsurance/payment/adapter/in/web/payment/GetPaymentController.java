package com.kakaoinsurance.payment.adapter.in.web.payment;

import com.kakaoinsurance.payment.application.port.in.payment.GetPaymentUseCase;
import com.kakaoinsurance.payment.common.properties.StringProperties;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;
import static com.kakaoinsurance.payment.common.utils.CommonUtil.maskString;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class GetPaymentController {

    private final GetPaymentUseCase getPaymentUseCase;
    private final StringProperties stringProperties;

    @GetMapping("/{managementId}")
    public ApiUtil.ApiResult<GetPaymentResponse> getPaymentByManagementId(@PathVariable String managementId) {
        Payment payment = getPaymentUseCase.getPaymentByManagementId(managementId);
        GetPaymentResponse paymentResponse = GetPaymentResponse.from(payment);
        paymentResponse.setCardNumber(maskString(paymentResponse.getCardNumber(), stringProperties.getMasking()));
        return success(paymentResponse);
    }

}
