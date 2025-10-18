package com.oms.service;

import com.oms.dto.AuthorizationRequestDto;
import com.oms.dto.AuthorizationResponseDto;
import com.oms.dto.EmailRequestDto;
import com.oms.entity.OrderLine;
import com.oms.entity.SalesOrder;
import com.oms.entity.Shipping;
import com.oms.repository.SalesOrderRepository;
import com.oms.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ModifyFulfillmentService {

    @Autowired
    SalesOrderRepository salesOrderRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    DinersPaymentService dinersPaymentService;

    @Autowired
    EmailService emailService;

    @Autowired
    ShippingService shippingService;

    @Autowired
    Logger logger;

    public SalesOrder modifyToShipping(String lineItemId, SalesOrder salesOrder) {
        logger.log(this.getClass().getName());
        if (salesOrder != null && !StringUtils.isEmpty(salesOrder.getCustomerOrderId())) {
            SalesOrder originalSalesOrder = salesOrderRepository.getOne(salesOrder.getCustomerOrderId());
            OrderLine orderLine = fetchOrderLineFromItemId(lineItemId, originalSalesOrder);
            double shippingAmount = getShippingAmount(orderLine.getCustomerSKU());
            if (orderLine != null && orderLine.getCharges() != null && orderLine.getCharges().getTotalCharges() != null) {
                if (orderLine.getCharges().getTotalCharges() > 0) {
                    AuthorizationRequestDto authorizationRequestDto =
                            new AuthorizationRequestDto(originalSalesOrder.getPaymentInfo().getCardType(), "23445567"
                                    , "12/24", "123", shippingAmount);
                    AuthorizationResponseDto responseDto = paymentService.authorize(authorizationRequestDto);
                    dinersPaymentService.authorize(authorizationRequestDto);
                    if (responseDto != null && !StringUtils.isEmpty(responseDto.getId()) && !StringUtils.isEmpty(responseDto.getAmount())) {
                        originalSalesOrder.getPaymentInfo().setAuthorizedAmount(originalSalesOrder.getPaymentInfo().getAuthorizedAmount() + responseDto.getAmount());
                        salesOrderRepository.save(originalSalesOrder);
                        emailService.sendEmail(buildEmailRequest(originalSalesOrder));
                        BeanUtils.copyProperties(originalSalesOrder, salesOrder);
                    }
                }
            }
        }
        return salesOrder;
    }

    public OrderLine fetchOrderLineFromItemId(String lineItemId , SalesOrder salesOrder) {
        List<OrderLine> orderLines = salesOrder.getOrderLines();
        OrderLine orderLine = orderLines.stream().filter(line -> line.getLineItemId().equalsIgnoreCase(lineItemId)).findAny().orElse(null);
        return orderLine;
    }

    public EmailRequestDto buildEmailRequest(SalesOrder salesOrder) {
        return new EmailRequestDto("1234","Modify fulfillment","Your line item have been successfully modified for fulfillment","Modify to shipping from store pickup");
    }

    public SalesOrder modifyToStorePickup(String lineItemId,SalesOrder salesOrder) {
        logger.log(this.getClass().getName());
        if(salesOrder != null && !StringUtils.isEmpty(salesOrder.getCustomerOrderId())){
            SalesOrder originalSalesOrder = salesOrderRepository.getOne(salesOrder.getCustomerOrderId());
            OrderLine orderLine = fetchOrderLineFromItemId(lineItemId ,originalSalesOrder);
            double shippingAmount = getShippingAmount(orderLine.getCustomerSKU());
            if(orderLine != null && orderLine.getCharges() != null && orderLine.getCharges().getTotalCharges() != null){
                if(orderLine.getCharges().getTotalCharges() > 0) {
                    AuthorizationRequestDto authorizationRequestDto =
                            new AuthorizationRequestDto(originalSalesOrder.getPaymentInfo().getCardType(), "23445567"
                                    , "12/24", "123", shippingAmount);
                    AuthorizationResponseDto responseDto = paymentService.reverseAuth(authorizationRequestDto);
                    dinersPaymentService.reverseAuth(authorizationRequestDto);
                    if(responseDto != null && !StringUtils.isEmpty(responseDto.getId()) && !StringUtils.isEmpty(responseDto.getAmount())){
                        originalSalesOrder.getPaymentInfo().setAuthorizedAmount(originalSalesOrder.getPaymentInfo().getAuthorizedAmount() - responseDto.getAmount());
                        salesOrderRepository.save(originalSalesOrder);
                        emailService.sendEmail(buildEmailRequest(originalSalesOrder));
                        BeanUtils.copyProperties(originalSalesOrder, salesOrder);
                    }
                }
            }
        }

        return salesOrder;
    }

    private double getShippingAmount(String skuId) {
        Shipping shipping = shippingService.fetchShippingCharges(skuId);
        if (shipping != null) {
            return shipping.getStandardShipping();
        }

        return 0;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
