package com.oms.integrations;

import com.oms.dto.AddressValidationRequestDto;
import com.oms.dto.AddressValidationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AddressValidatorHttpClientImpl implements AddressValidatorHttpClient{

    @Override
    public AddressValidationResponseDto standardizeAddress(AddressValidationRequestDto addressValidationRequestDto) {
        //call real time service
        //call mock service
        return mockStandardizedAddressResponse(addressValidationRequestDto);
    }

    private AddressValidationResponseDto mockStandardizedAddressResponse(AddressValidationRequestDto addressValidationRequestDto) {
        AddressValidationResponseDto addressValidationResponseDto = new AddressValidationResponseDto(
                "APT 105",
                "4101 W 98th ST",
                "BLOOMINGTON",
                "MN",
                "55437",
                "US");
        return addressValidationResponseDto;
    }
}
