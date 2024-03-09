package com.kakaoinsurance.payment.adapter.in.web;

import com.kakaoinsurance.payment.application.port.in.GetPaymentUseCase;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import com.kakaoinsurance.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class GetPaymentController {

    private final GetPaymentUseCase getPaymentUseCase;

    @GetMapping("/{managementId}")
    public ApiUtil.ApiResult<GetPaymentResponse> getPaymentByManagementId(@PathVariable String managementId) {
        Payment payment = getPaymentUseCase.getPaymentByManagementId(managementId);
        return success(GetPaymentResponse.from(payment));
    }

}
