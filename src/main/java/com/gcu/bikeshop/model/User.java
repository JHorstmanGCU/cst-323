package com.gcu.bikeshop.model;

/**
 * Stores customer data from the users table.
 */
public class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;

    public User() {
    }

    public User(Long userId, String firstName, String lastName, String email, String phone, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Combines first and last name for the page display.
     *
     * @return customer's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
