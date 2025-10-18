package com.oms.integrations;

import com.oms.dto.AddressValidationRequestDto;
import com.oms.dto.AddressValidationResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AddressValidatorHttpClient {

    AddressValidationResponseDto standardizeAddress(AddressValidationRequestDto addressValidationRequestDto);

}
