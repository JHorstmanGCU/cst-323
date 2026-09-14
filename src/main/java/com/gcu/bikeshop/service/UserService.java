package com.gcu.bikeshop.service;

import com.gcu.bikeshop.data.UserDataService;
import com.gcu.bikeshop.model.User;
import com.gcu.bikeshop.model.UserForm;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handles customer business logic.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDataService userDataService;

    public UserService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    /**
     * Gets all customers from the data service.
     *
     * @return list of customers
     */
    public List<User> getAllUsers() {
        logger.info("Entering UserService.getAllUsers");
        List<User> users = userDataService.findAll();
        logger.info("Leaving UserService.getAllUsers with {} users", users.size());
        return users;
    }

    /**
     * Gets one customer from the data service.
     *
     * @param userId customer id
     * @return customer when found
     */
    public Optional<User> getUser(Long userId) {
        logger.info("Entering UserService.getUser with userId={}", userId);
        Optional<User> user = userDataService.findById(userId);
        logger.info("Leaving UserService.getUser with found={}", user.isPresent());
        return user;
    }

    /**
     * Sends the new customer form to the data service.
     *
     * @param userForm customer form data
     */
    public void createUser(UserForm userForm) {
        logger.info("Entering UserService.createUser");
        userDataService.create(userForm);
        logger.info("Leaving UserService.createUser");
    }

    /**
     * Sends the customer update to the data service.
     *
     * @param userForm customer form data
     * @param userId customer id
     */
    public void updateUser(UserForm userForm, Long userId) {
        logger.info("Entering UserService.updateUser with userId={}", userId);
        userDataService.update(userForm, userId);
        logger.info("Leaving UserService.updateUser with userId={}", userId);
    }

    /**
     * Sends the customer delete request to the data service.
     *
     * @param userId customer id
     */
    public void deleteUser(Long userId) {
        logger.info("Entering UserService.deleteUser with userId={}", userId);
        userDataService.delete(userId);
        logger.info("Leaving UserService.deleteUser with userId={}", userId);
    }
}
