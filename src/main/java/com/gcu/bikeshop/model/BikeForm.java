package com.gcu.bikeshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Holds the bike form fields and validation rules.
 */
public class BikeForm {
    @NotNull(message = "Customer is required.")
    private Long userId;

    @NotBlank(message = "Brand is required.")
    @Size(max = 50, message = "Brand must be 50 characters or less.")
    private String brand;

    @NotBlank(message = "Model is required.")
    @Size(max = 50, message = "Model must be 50 characters or less.")
    private String model;

    @Size(max = 100, message = "Serial number must be 100 characters or less.")
    private String serialNumber;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
