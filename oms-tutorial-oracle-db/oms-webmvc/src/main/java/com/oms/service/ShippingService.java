package com.oms.service;

import com.oms.entity.Shipping;
import com.oms.repository.ShippingRepository;
import com.oms.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ShippingService {

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    Logger logger;

    public Shipping fetchShippingCharges(String skuId) {
        logger.log(this.getClass().getName());
        return fetchShippingChargesFromRepository(skuId);
    }

    private Shipping fetchShippingChargesFromRepository(String skuId) {
        return shippingRepository.findById(skuId).get();
    }

    public Shipping createShipping(Shipping shipping) {
        logger.log(this.getClass().getName());
        return shippingRepository.save(shipping);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
