package com.gcu.bikeshop.controller;

import com.gcu.bikeshop.model.UserForm;
import com.gcu.bikeshop.model.User;
import com.gcu.bikeshop.service.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles customer pages and customer form actions.
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows all customers saved in the database.
     *
     * @param model page data sent to the view
     * @return the customer list template
     */
    @GetMapping
    public String list(Model model) {
        logger.info("Entering CustomerController.list");
        model.addAttribute("pageTitle", "Customers");

        try {
            model.addAttribute("users", userService.getAllUsers());
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in CustomerController ({})", exception.getClass().getSimpleName());
            model.addAttribute("users", java.util.List.of());
            model.addAttribute("databaseError", true);
        }

        logger.info("Leaving CustomerController.list");
        return "customers/list";
    }

    /**
     * Opens the form to add a new customer.
     *
     * @param model page data sent to the view
     * @return the new customer template
     */
    @GetMapping("/new")
    public String newCustomer(Model model) {
        logger.info("Entering CustomerController.newCustomer");
        model.addAttribute("pageTitle", "Add Customer");
        model.addAttribute("userForm", new UserForm());
        logger.info("Leaving CustomerController.newCustomer");
        return "customers/new";
    }

    /**
     * Checks the form and saves the customer.
     *
     * @param userForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when saved or the form when there are errors
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model) {
        logger.info("Entering CustomerController.create");

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Customer");
            logger.info("Leaving CustomerController.create with validation errors");
            return "customers/new";
        }

        try {
            userService.createUser(userForm);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.rejectValue("email", "duplicate", "A customer with this email already exists.");
            model.addAttribute("pageTitle", "Add Customer");
            logger.info("Leaving CustomerController.create with duplicate email");
            return "customers/new";
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in CustomerController ({})", exception.getClass().getSimpleName());
            model.addAttribute("pageTitle", "Add Customer");
            model.addAttribute("databaseError", true);
            logger.info("Leaving CustomerController.create with database error");
            return "customers/new";
        }

        logger.info("Leaving CustomerController.create with success");
        return "redirect:/customers?created";
    }

    /**
     * Opens the form to edit one customer.
     *
     * @param userId customer id from the URL
     * @param model page data sent to the view
     * @return the edit customer template
     */
    @GetMapping("/{userId}/edit")
    public String edit(@PathVariable Long userId, Model model) {
        logger.info("Entering CustomerController.edit with userId={}", userId);

        try {
            Optional<User> user = userService.getUser(userId);
            if (user.isEmpty()) {
                logger.info("Leaving CustomerController.edit with missing customer");
                return "redirect:/customers?missing";
            }

            UserForm userForm = new UserForm();
            userForm.setFirstName(user.get().getFirstName());
            userForm.setLastName(user.get().getLastName());
            userForm.setEmail(user.get().getEmail());
            userForm.setPhone(user.get().getPhone());

            model.addAttribute("pageTitle", "Edit Customer");
            model.addAttribute("user", user.get());
            model.addAttribute("userForm", userForm);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in CustomerController ({})", exception.getClass().getSimpleName());
            logger.info("Leaving CustomerController.edit with database error");
            return "redirect:/customers?databaseError";
        }

        logger.info("Leaving CustomerController.edit");
        return "customers/edit";
    }

    /**
     * Checks the form and saves the customer changes.
     *
     * @param userId customer id from the URL
     * @param userForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when updated or the form when there are errors
     */
    @PostMapping("/{userId}")
    public String update(
            @PathVariable Long userId,
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Entering CustomerController.update with userId={}", userId);

        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty()) {
            logger.info("Leaving CustomerController.update with missing customer");
            return "redirect:/customers?missing";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Customer");
            model.addAttribute("user", user.get());
            logger.info("Leaving CustomerController.update with validation errors");
            return "customers/edit";
        }

        try {
            userService.updateUser(userForm, userId);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.rejectValue("email", "duplicate", "A customer with this email already exists.");
            model.addAttribute("pageTitle", "Edit Customer");
            model.addAttribute("user", user.get());
            logger.info("Leaving CustomerController.update with duplicate email");
            return "customers/edit";
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in CustomerController ({})", exception.getClass().getSimpleName());
            model.addAttribute("pageTitle", "Edit Customer");
            model.addAttribute("user", user.get());
            model.addAttribute("databaseError", true);
            logger.info("Leaving CustomerController.update with database error");
            return "customers/edit";
        }

        logger.info("Leaving CustomerController.update with success");
        return "redirect:/customers?updated";
    }

    /**
     * Deletes one customer.
     *
     * @param userId customer id from the URL
     * @return redirect back to the customer list
     */
    @PostMapping("/{userId}/delete")
    public String delete(@PathVariable Long userId) {
        logger.info("Entering CustomerController.delete with userId={}", userId);

        try {
            userService.deleteUser(userId);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in CustomerController ({})", exception.getClass().getSimpleName());
            logger.info("Leaving CustomerController.delete with database error");
            return "redirect:/customers?databaseError";
        }

        logger.info("Leaving CustomerController.delete with success");
        return "redirect:/customers?deleted";
    }
}
