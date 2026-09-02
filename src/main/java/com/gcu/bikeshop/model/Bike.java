package com.gcu.bikeshop.model;

/**
 * Stores bike data from the bikes table.
 */
public class Bike {
    private Long bikeId;
    private Long userId;
    private String customerName;
    private String brand;
    private String model;
    private String serialNumber;

    public Bike() {
    }

    public Bike(Long bikeId, Long userId, String customerName, String brand, String model, String serialNumber) {
        this.bikeId = bikeId;
        this.userId = userId;
        this.customerName = customerName;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public Long getBikeId() {
        return bikeId;
    }

    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    /**
     * Combines brand and model for the page display.
     *
     * @return bike display name
     */
    public String getBikeName() {
        return brand + " " + model;
    }
}
