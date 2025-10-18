package com.oms.service;

import com.oms.dto.AuthorizationRequestDto;
import com.oms.dto.AuthorizationResponseDto;
import com.oms.integrations.PaymentHttpClient;
import com.oms.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PaymentService {

    private PaymentHttpClient visaPaymentHttpClient;

    private PaymentHttpClient amexPaymentHttpClient;

    @Autowired
    Logger logger;

    @Autowired
    public PaymentService(PaymentHttpClient visaPaymentHttpClient, PaymentHttpClient amexPaymentHttpClient) {
        this.visaPaymentHttpClient = visaPaymentHttpClient;
        this.amexPaymentHttpClient = amexPaymentHttpClient;
    }

    public AuthorizationResponseDto authorize(AuthorizationRequestDto authorizationRequestDto) {
        logger.log(this.getClass().getName());
        if (authorizationRequestDto.getCardType().equals("VISA")) {
            return visaPaymentHttpClient.authorize(authorizationRequestDto);
        } else {
            return amexPaymentHttpClient.authorize(authorizationRequestDto);
        }
    }

    public AuthorizationResponseDto reverseAuth(AuthorizationRequestDto authorizationRequestDto) {
        logger.log(this.getClass().getName());
        if (authorizationRequestDto.getCardType().equals("VISA")) {
            return visaPaymentHttpClient.reverseAuth(authorizationRequestDto);
        } else {
            return amexPaymentHttpClient.reverseAuth(authorizationRequestDto);
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
