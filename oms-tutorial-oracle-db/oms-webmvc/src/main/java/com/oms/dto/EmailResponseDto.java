package com.oms.dto;

public class EmailResponseDto {

    private String salesOrderNumber;
    private String emailType;
    private String emailStatus;

    public EmailResponseDto(String salesOrderNumber, String emailType, String emailStatus) {
        this.salesOrderNumber = salesOrderNumber;
        this.emailType = emailType;
        this.emailStatus = emailStatus;
    }

    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }
}

