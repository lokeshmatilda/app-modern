package com.oms.service;

import com.oms.dto.AuthorizationRequestDto;
import com.oms.dto.AuthorizationResponseDto;
import com.oms.entity.LineCharges;
import com.oms.entity.OrderLine;
import com.oms.entity.PaymentInfo;
import com.oms.entity.SalesOrder;
import com.oms.entity.Shipping;
import com.oms.repository.SalesOrderRepository;
import com.oms.util.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ModifyFulfillmentServiceTest {

    @Mock
    SalesOrderRepository salesOrderRepository;
    @Mock
    PaymentService paymentService;
    @Mock
    DinersPaymentService dinersPaymentService;
    @Mock
    EmailService emailService;

    @Mock
    ShippingService shippingService;

    ModifyFulfillmentService modifyFulfillmentService = new ModifyFulfillmentService();

    @Before
    public void setUp() {
        modifyFulfillmentService.salesOrderRepository = salesOrderRepository;
        modifyFulfillmentService.paymentService = paymentService;
        modifyFulfillmentService.dinersPaymentService = dinersPaymentService;
        modifyFulfillmentService.emailService = emailService;
        modifyFulfillmentService.shippingService = shippingService;
        modifyFulfillmentService.setLogger(new Logger());
    }

    @Test
    public void testModifyShipping() {
        SalesOrder salesOrder = getSalesOrder();

        Mockito.when(salesOrderRepository.getOne("5678")).thenReturn(salesOrder);
        Mockito.when(paymentService.authorize(Mockito.any()))
                .thenReturn(new AuthorizationResponseDto("123",7.00,"SUCCESS"));
        Mockito.when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);

        SalesOrder modifiedOrder = modifyFulfillmentService.modifyToShipping("1234",salesOrder);

        Assert.assertNotNull(modifiedOrder);
    }

    @Test
    public void testModifyStorePickup() {
        SalesOrder salesOrder = getSalesOrder();
        AuthorizationResponseDto returnAuth = new AuthorizationResponseDto("123",7.00,"SUCCESS");
        Shipping shipping = new Shipping("SKU1", 8.0, 9.0, 10.0);
        Mockito.when(salesOrderRepository.getOne("5678")).thenReturn(salesOrder);
        Mockito.when(paymentService.reverseAuth(new AuthorizationRequestDto("VISA", "23445567", "12/24", "123", shipping.getStandardShipping())))
                .thenReturn(returnAuth);
        Mockito.when(dinersPaymentService.reverseAuth(new AuthorizationRequestDto("VISA", "23445567", "12/24", "123", shipping.getStandardShipping())))
                .thenReturn(returnAuth);
        Mockito.when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);
        Mockito.when(shippingService.fetchShippingCharges("SKU1")).thenReturn(shipping);

        double authorizedB4 = salesOrder.getPaymentInfo().getAuthorizedAmount();
        SalesOrder modifiedOrder = modifyFulfillmentService.modifyToStorePickup("1234",salesOrder);

        Assert.assertNotNull(modifiedOrder);
        double authorizedAf = modifiedOrder.getPaymentInfo().getAuthorizedAmount();
        Assert.assertEquals(authorizedB4 - returnAuth.getAmount(), authorizedAf, 0.0);
    }

    private SalesOrder getSalesOrder() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerOrderId("5678");
        List<OrderLine> orderLines = new ArrayList<>();
        OrderLine orderLine = new OrderLine();
        orderLine.setLineItemId("1234");
        orderLine.setCustomerSKU("SKU1");
        LineCharges charges = new LineCharges();
        charges.setTotalCharges(7.00);
        orderLine.setCharges(charges);
        orderLines.add(orderLine);
        salesOrder.setOrderLines(orderLines);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setAuthorizedAmount(100.00);
        paymentInfo.setCardType("VISA");
        salesOrder.setPaymentInfo(paymentInfo);
        return salesOrder;
    }

}
