package com.gcu.bikeshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Stores service order data from the service_orders table.
 */
public class ServiceOrder {
    private Long orderId;
    private Long bikeId;
    private Long partId;
    private String customerName;
    private String bikeName;
    private String partName;
    private String serviceType;
    private String problemDescription;
    private OrderStatus status;
    private LocalDate dropoffDate;
    private BigDecimal totalAmount;

    public ServiceOrder() {
    }

    public ServiceOrder(
            Long orderId,
            Long bikeId,
            Long partId,
            String customerName,
            String bikeName,
            String partName,
            String serviceType,
            String problemDescription,
            OrderStatus status,
            LocalDate dropoffDate,
            BigDecimal totalAmount
    ) {
        this.orderId = orderId;
        this.bikeId = bikeId;
        this.partId = partId;
        this.customerName = customerName;
        this.bikeName = bikeName;
        this.partName = partName;
        this.serviceType = serviceType;
        this.problemDescription = problemDescription;
        this.status = status;
        this.dropoffDate = dropoffDate;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBikeId() {
        return bikeId;
    }

    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getDropoffDate() {
        return dropoffDate;
    }

    public void setDropoffDate(LocalDate dropoffDate) {
        this.dropoffDate = dropoffDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
