package com.oms.service;

import com.oms.dto.AddressValidationRequestDto;
import com.oms.dto.AddressValidationResponseDto;
import com.oms.integrations.AddressValidatorHttpClient;
import com.oms.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Transactional
public class AddressValidationService {

    @Autowired
    AddressValidatorHttpClient addressValidatorHttpClient;

    @Autowired
    Logger logger;

    AddressValidationResponseDto validateAddress(AddressValidationRequestDto addressValidationRequestDto) {
        logger.log(this.getClass().getName());
        AddressValidationResponseDto responseDto = new AddressValidationResponseDto();
        if (isValidRequestForStandardization(addressValidationRequestDto)) {
            responseDto = addressValidatorHttpClient.standardizeAddress(addressValidationRequestDto);
        }
        return responseDto;
    }

    private boolean isValidRequestForStandardization(AddressValidationRequestDto addressValidationRequestDto) {
        logger.log(this.getClass().getName());
        if (addressValidationRequestDto != null && !StringUtils.isEmpty(addressValidationRequestDto.getPostalCode())
                && !StringUtils.isEmpty(addressValidationRequestDto.getCity())) {
            return true;
        } else {
            return false;
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
