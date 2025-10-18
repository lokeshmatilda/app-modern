package com.oms.service;

import com.oms.dto.AddressValidationRequestDto;
import com.oms.dto.AddressValidationResponseDto;
import com.oms.integrations.AddressValidatorHttpClient;
import com.oms.util.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressValidationServiceTest {

    @Mock
    AddressValidatorHttpClient addressValidatorHttpClient;
    AddressValidationService addressValidationService = new AddressValidationService();

    @Before
    public void setUp() {
        addressValidationService.addressValidatorHttpClient = addressValidatorHttpClient;
        addressValidationService.setLogger(new Logger());
    }

    @Test
    public void validateAddressWithValidRequest() {
        AddressValidationResponseDto addressValidationResponseDto = new AddressValidationResponseDto(
                "4101 W 98th ST",
                "APT 101",
                "BLOOMINGTON",
                "MN",
                "55347",
                "US");
        AddressValidationRequestDto addressValidationRequestDto = new AddressValidationRequestDto("410 W 98th STREET","APARTMENT 101","BLOOMINGTON","MN","55347","US");

        when(addressValidatorHttpClient.standardizeAddress(addressValidationRequestDto)).thenReturn(addressValidationResponseDto);

        AddressValidationResponseDto responseDto = addressValidationService.validateAddress(addressValidationRequestDto);

        Assert.assertNotNull(responseDto);
        Assert.assertEquals(responseDto.getAddressLine1(),"4101 W 98th ST");

    }

    @Test
    public void validateAddressWithInValidRequest() {
        AddressValidationRequestDto addressValidationRequestDto = new AddressValidationRequestDto("410 W 98th STREET","APARTMENT 101",null,"MN","55347","US");
        AddressValidationResponseDto responseDto = addressValidationService.validateAddress(addressValidationRequestDto);
        Assert.assertNull(responseDto.getAddressLine1());
    }


}
