package com.gcu.bikeshop.model;

/**
 * Status choices for each service order.
 */
public enum OrderStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    READY("Ready");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the readable status name for the page.
     *
     * @return status display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
