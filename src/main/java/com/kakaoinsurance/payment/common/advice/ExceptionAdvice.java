package com.kakaoinsurance.payment.common.advice;

import com.kakaoinsurance.payment.common.advice.exceptions.*;
import com.kakaoinsurance.payment.common.utils.ApiUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kakaoinsurance.payment.common.utils.ApiUtil.fail;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiUtil.ApiResult<Void> defaultException(Exception e) {
        e.printStackTrace();
        return fail(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
        AlreadyPresentAccountException.class,
        NotMatchedPasswordException.class,
        NotValidMemberException.class,
        InvalidInstallmentMonthException.class,
        NotPresentPaymentException.class,
        ConstraintViolationException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ApiUtil.ApiResult<Void> badRequest(Exception e) {
        e.printStackTrace();
        return fail(e, BAD_REQUEST);
    }

}
