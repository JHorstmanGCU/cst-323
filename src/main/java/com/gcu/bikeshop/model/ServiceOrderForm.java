package com.gcu.bikeshop.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Holds the service order form fields and validation rules.
 */
public class ServiceOrderForm {
    @NotNull(message = "Bike is required.")
    private Long bikeId;

    private Long partId;

    @NotBlank(message = "Service type is required.")
    @Size(max = 75, message = "Service type must be 75 characters or less.")
    private String serviceType;

    @Size(max = 1000, message = "Problem description must be 1000 characters or less.")
    private String problemDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dropoffDate = LocalDate.now();

    @DecimalMin(value = "0.00", message = "Total amount cannot be negative.")
    private BigDecimal totalAmount = BigDecimal.ZERO;

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
