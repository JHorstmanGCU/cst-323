package com.gcu.bikeshop.model;

import java.math.BigDecimal;

/**
 * Stores part data from the parts table.
 */
public class Part {
    private Long partId;
    private String partName;
    private String partNumber;
    private BigDecimal price;
    private Integer quantity;

    public Part() {
    }

    public Part(Long partId, String partName, String partNumber, BigDecimal price, Integer quantity) {
        this.partId = partId;
        this.partName = partName;
        this.partNumber = partNumber;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
