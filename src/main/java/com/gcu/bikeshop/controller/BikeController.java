package com.gcu.bikeshop.controller;

import com.gcu.bikeshop.model.Bike;
import com.gcu.bikeshop.model.BikeForm;
import com.gcu.bikeshop.service.BikeService;
import com.gcu.bikeshop.service.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles bike pages and bike form actions.
 */
@Controller
@RequestMapping("/bikes")
public class BikeController {
    private static final Logger logger = LoggerFactory.getLogger(BikeController.class);
    private final BikeService bikeService;
    private final UserService userService;

    public BikeController(BikeService bikeService, UserService userService) {
        this.bikeService = bikeService;
        this.userService = userService;
    }

    /**
     * Shows all bikes and the customer they belong to.
     *
     * @param model page data sent to the view
     * @return the bike list template
     */
    @GetMapping
    public String list(Model model) {
        logger.info("Entering BikeController.list");
        model.addAttribute("pageTitle", "Bikes");

        try {
            model.addAttribute("bikes", bikeService.getAllBikes());
        } catch (DataAccessException exception) {
            model.addAttribute("bikes", java.util.List.of());
            model.addAttribute("databaseError", true);
        }

        logger.info("Leaving BikeController.list");
        return "bikes/list";
    }

    /**
     * Opens the form to add a new bike.
     *
     * @param model page data sent to the view
     * @return the new bike template
     */
    @GetMapping("/new")
    public String newBike(Model model) {
        logger.info("Entering BikeController.newBike");
        prepareForm(model, new BikeForm());
        logger.info("Leaving BikeController.newBike");
        return "bikes/new";
    }

    /**
     * Checks the form and saves the bike.
     *
     * @param bikeForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when saved or the form when there are errors
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("bikeForm") BikeForm bikeForm, BindingResult bindingResult, Model model) {
        logger.info("Entering BikeController.create");

        if (bindingResult.hasErrors()) {
            prepareForm(model, bikeForm);
            logger.info("Leaving BikeController.create with validation errors");
            return "bikes/new";
        }

        try {
            bikeService.createBike(bikeForm);
        } catch (DataAccessException exception) {
            prepareForm(model, bikeForm);
            model.addAttribute("databaseError", true);
            logger.info("Leaving BikeController.create with database error");
            return "bikes/new";
        }

        logger.info("Leaving BikeController.create with success");
        return "redirect:/bikes?created";
    }

    /**
     * Opens the form to edit one bike.
     *
     * @param bikeId bike id from the URL
     * @param model page data sent to the view
     * @return the edit bike template
     */
    @GetMapping("/{bikeId}/edit")
    public String edit(@PathVariable Long bikeId, Model model) {
        logger.info("Entering BikeController.edit with bikeId={}", bikeId);

        try {
            Optional<Bike> bike = bikeService.getBike(bikeId);
            if (bike.isEmpty()) {
                logger.info("Leaving BikeController.edit with missing bike");
                return "redirect:/bikes?missing";
            }

            BikeForm bikeForm = new BikeForm();
            bikeForm.setUserId(bike.get().getUserId());
            bikeForm.setBrand(bike.get().getBrand());
            bikeForm.setModel(bike.get().getModel());
            bikeForm.setSerialNumber(bike.get().getSerialNumber());
            prepareEditForm(model, bike.get(), bikeForm);
        } catch (DataAccessException exception) {
            logger.info("Leaving BikeController.edit with database error");
            return "redirect:/bikes?databaseError";
        }

        logger.info("Leaving BikeController.edit");
        return "bikes/edit";
    }

    /**
     * Checks the form and saves the bike changes.
     *
     * @param bikeId bike id from the URL
     * @param bikeForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when updated or the form when there are errors
     */
    @PostMapping("/{bikeId}")
    public String update(
            @PathVariable Long bikeId,
            @Valid @ModelAttribute("bikeForm") BikeForm bikeForm,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Entering BikeController.update with bikeId={}", bikeId);

        Optional<Bike> bike = bikeService.getBike(bikeId);
        if (bike.isEmpty()) {
            logger.info("Leaving BikeController.update with missing bike");
            return "redirect:/bikes?missing";
        }

        if (bindingResult.hasErrors()) {
            prepareEditForm(model, bike.get(), bikeForm);
            logger.info("Leaving BikeController.update with validation errors");
            return "bikes/edit";
        }

        try {
            bikeService.updateBike(bikeForm, bikeId);
        } catch (DataAccessException exception) {
            prepareEditForm(model, bike.get(), bikeForm);
            model.addAttribute("databaseError", true);
            logger.info("Leaving BikeController.update with database error");
            return "bikes/edit";
        }

        logger.info("Leaving BikeController.update with success");
        return "redirect:/bikes?updated";
    }

    /**
     * Deletes one bike.
     *
     * @param bikeId bike id from the URL
     * @return redirect back to the bike list
     */
    @PostMapping("/{bikeId}/delete")
    public String delete(@PathVariable Long bikeId) {
        logger.info("Entering BikeController.delete with bikeId={}", bikeId);

        try {
            bikeService.deleteBike(bikeId);
        } catch (DataAccessException exception) {
            logger.info("Leaving BikeController.delete with database error");
            return "redirect:/bikes?databaseError";
        }

        logger.info("Leaving BikeController.delete with success");
        return "redirect:/bikes?deleted";
    }

    /**
     * Adds the data needed for the bike form dropdown.
     *
     * @param model page data sent to the view
     * @param bikeForm form object used by the page
     */
    private void prepareForm(Model model, BikeForm bikeForm) {
        model.addAttribute("pageTitle", "Add Bike");
        model.addAttribute("bikeForm", bikeForm);

        try {
            model.addAttribute("users", userService.getAllUsers());
        } catch (DataAccessException exception) {
            model.addAttribute("users", java.util.List.of());
            model.addAttribute("databaseError", true);
        }
    }

    /**
     * Adds the data needed for the bike edit form.
     *
     * @param model page data sent to the view
     * @param bike bike being edited
     * @param bikeForm form object used by the page
     */
    private void prepareEditForm(Model model, Bike bike, BikeForm bikeForm) {
        model.addAttribute("pageTitle", "Edit Bike");
        model.addAttribute("bike", bike);
        model.addAttribute("bikeForm", bikeForm);

        try {
            model.addAttribute("users", userService.getAllUsers());
        } catch (DataAccessException exception) {
            model.addAttribute("users", java.util.List.of());
            model.addAttribute("databaseError", true);
        }
    }
}
