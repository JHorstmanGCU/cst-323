package com.gcu.bikeshop.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Holds the fields used when updating a service order.
 */
public class ServiceOrderUpdateForm {
    @NotNull(message = "Status is required.")
    private OrderStatus status;

    @DecimalMin(value = "0.00", message = "Total amount cannot be negative.")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
