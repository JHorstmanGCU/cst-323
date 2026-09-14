package com.gcu.bikeshop.controller;

import com.gcu.bikeshop.model.Part;
import com.gcu.bikeshop.model.PartForm;
import com.gcu.bikeshop.service.PartService;
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
 * Handles part inventory pages and part form actions.
 */
@Controller
@RequestMapping("/parts")
public class PartController {
    private static final Logger logger = LoggerFactory.getLogger(PartController.class);
    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    /**
     * Shows all parts in inventory.
     *
     * @param model page data sent to the view
     * @return the part list template
     */
    @GetMapping
    public String list(Model model) {
        logger.info("Entering PartController.list");
        model.addAttribute("pageTitle", "Parts");

        try {
            model.addAttribute("parts", partService.getAllParts());
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in PartController ({})", exception.getClass().getSimpleName());
            model.addAttribute("parts", java.util.List.of());
            model.addAttribute("databaseError", true);
        }

        logger.info("Leaving PartController.list");
        return "parts/list";
    }

    /**
     * Opens the form to add a new part.
     *
     * @param model page data sent to the view
     * @return the new part template
     */
    @GetMapping("/new")
    public String newPart(Model model) {
        logger.info("Entering PartController.newPart");
        model.addAttribute("pageTitle", "Add Part");
        model.addAttribute("partForm", new PartForm());
        logger.info("Leaving PartController.newPart");
        return "parts/new";
    }

    /**
     * Checks the form and saves the part.
     *
     * @param partForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when saved or the form when there are errors
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("partForm") PartForm partForm, BindingResult bindingResult, Model model) {
        logger.info("Entering PartController.create");

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Part");
            logger.info("Leaving PartController.create with validation errors");
            return "parts/new";
        }

        try {
            partService.createPart(partForm);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in PartController ({})", exception.getClass().getSimpleName());
            model.addAttribute("pageTitle", "Add Part");
            model.addAttribute("databaseError", true);
            logger.info("Leaving PartController.create with database error");
            return "parts/new";
        }

        logger.info("Leaving PartController.create with success");
        return "redirect:/parts?created";
    }

    /**
     * Opens the form to edit one part.
     *
     * @param partId part id from the URL
     * @param model page data sent to the view
     * @return the edit part template
     */
    @GetMapping("/{partId}/edit")
    public String edit(@PathVariable Long partId, Model model) {
        logger.info("Entering PartController.edit with partId={}", partId);

        try {
            Optional<Part> part = partService.getPart(partId);
            if (part.isEmpty()) {
                logger.info("Leaving PartController.edit with missing part");
                return "redirect:/parts?missing";
            }

            PartForm partForm = new PartForm();
            partForm.setPartName(part.get().getPartName());
            partForm.setPartNumber(part.get().getPartNumber());
            partForm.setPrice(part.get().getPrice());
            partForm.setQuantity(part.get().getQuantity());

            model.addAttribute("pageTitle", "Edit Part");
            model.addAttribute("part", part.get());
            model.addAttribute("partForm", partForm);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in PartController ({})", exception.getClass().getSimpleName());
            logger.info("Leaving PartController.edit with database error");
            return "redirect:/parts?databaseError";
        }

        logger.info("Leaving PartController.edit");
        return "parts/edit";
    }

    /**
     * Checks the form and saves the part changes.
     *
     * @param partId part id from the URL
     * @param partForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when updated or the form when there are errors
     */
    @PostMapping("/{partId}")
    public String update(
            @PathVariable Long partId,
            @Valid @ModelAttribute("partForm") PartForm partForm,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Entering PartController.update with partId={}", partId);

        Optional<Part> part = partService.getPart(partId);
        if (part.isEmpty()) {
            logger.info("Leaving PartController.update with missing part");
            return "redirect:/parts?missing";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Part");
            model.addAttribute("part", part.get());
            logger.info("Leaving PartController.update with validation errors");
            return "parts/edit";
        }

        try {
            partService.updatePart(partForm, partId);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in PartController ({})", exception.getClass().getSimpleName());
            model.addAttribute("pageTitle", "Edit Part");
            model.addAttribute("part", part.get());
            model.addAttribute("databaseError", true);
            logger.info("Leaving PartController.update with database error");
            return "parts/edit";
        }

        logger.info("Leaving PartController.update with success");
        return "redirect:/parts?updated";
    }

    /**
     * Deletes one part.
     *
     * @param partId part id from the URL
     * @return redirect back to the part list
     */
    @PostMapping("/{partId}/delete")
    public String delete(@PathVariable Long partId) {
        logger.info("Entering PartController.delete with partId={}", partId);

        try {
            partService.deletePart(partId);
        } catch (DataAccessException exception) {
            logger.error("Database operation failed in PartController ({})", exception.getClass().getSimpleName());
            logger.info("Leaving PartController.delete with database error");
            return "redirect:/parts?databaseError";
        }

        logger.info("Leaving PartController.delete with success");
        return "redirect:/parts?deleted";
    }
}
