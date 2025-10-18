package com.oms.dto;

public class InventoryRequestDto {

    private String skuId;
    private String storeId;

    public InventoryRequestDto(String skuId, String storeId) {
        this.skuId = skuId;
        this.storeId = storeId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
