package com.oms.integrations;

import org.springframework.stereotype.Component;

@Component
public class MastercardPaymentHttpClient extends AbstractPaymentHttpClient {
    public MastercardPaymentHttpClient() {
        super(13.0);
    }
}
