package com.gcu.bikeshop.controller;

import com.gcu.bikeshop.model.OrderStatus;
import com.gcu.bikeshop.model.ServiceOrder;
import com.gcu.bikeshop.model.ServiceOrderForm;
import com.gcu.bikeshop.model.ServiceOrderUpdateForm;
import com.gcu.bikeshop.service.BikeService;
import com.gcu.bikeshop.service.PartService;
import com.gcu.bikeshop.service.ServiceOrderService;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles service order pages and service order form actions.
 */
@Controller
@RequestMapping("/service-orders")
public class ServiceOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderController.class);
    private final ServiceOrderService serviceOrderService;
    private final BikeService bikeService;
    private final PartService partService;

    public ServiceOrderController(
            ServiceOrderService serviceOrderService,
            BikeService bikeService,
            PartService partService
    ) {
        this.serviceOrderService = serviceOrderService;
        this.bikeService = bikeService;
        this.partService = partService;
    }

    /**
     * Shows all service orders for the shop, with an optional status filter.
     *
     * @param status optional status filter from the query string
     * @param model page data sent to the view
     * @return the service order list template
     */
    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        logger.info("Entering ServiceOrderController.list with status={}", status);
        model.addAttribute("pageTitle", "Service Orders");
        model.addAttribute("statuses", OrderStatus.values());

        OrderStatus selectedStatus = parseStatus(status);
        model.addAttribute("selectedStatus", selectedStatus == null ? "" : selectedStatus.name());

        if (status != null && !status.isBlank() && selectedStatus == null) {
            model.addAttribute("filterError", true);
        }

        try {
            if (selectedStatus == null) {
                model.addAttribute("orders", serviceOrderService.getAllServiceOrders());
            } else {
                model.addAttribute("orders", serviceOrderService.getServiceOrdersByStatus(selectedStatus));
            }
        } catch (DataAccessException exception) {
            model.addAttribute("orders", java.util.List.of());
            model.addAttribute("databaseError", true);
        }

        logger.info("Leaving ServiceOrderController.list");
        return "service-orders/list";
    }

    /**
     * Opens the form to create a service order.
     *
     * @param model page data sent to the view
     * @return the new service order template
     */
    @GetMapping("/new")
    public String newServiceOrder(Model model) {
        logger.info("Entering ServiceOrderController.newServiceOrder");
        prepareCreateForm(model, new ServiceOrderForm());
        logger.info("Leaving ServiceOrderController.newServiceOrder");
        return "service-orders/new";
    }

    /**
     * Checks the form and saves the service order.
     *
     * @param serviceOrderForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when saved or the form when there are errors
     */
    @PostMapping
    public String create(
            @Valid @ModelAttribute("serviceOrderForm") ServiceOrderForm serviceOrderForm,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Entering ServiceOrderController.create");

        if (bindingResult.hasErrors()) {
            prepareCreateForm(model, serviceOrderForm);
            logger.info("Leaving ServiceOrderController.create with validation errors");
            return "service-orders/new";
        }

        try {
            serviceOrderService.createServiceOrder(serviceOrderForm);
        } catch (DataAccessException exception) {
            prepareCreateForm(model, serviceOrderForm);
            model.addAttribute("databaseError", true);
            logger.info("Leaving ServiceOrderController.create with database error");
            return "service-orders/new";
        }

        logger.info("Leaving ServiceOrderController.create with success");
        return "redirect:/service-orders?created";
    }

    /**
     * Opens the update page for one service order.
     *
     * @param orderId order id from the URL
     * @param model page data sent to the view
     * @return the edit service order template
     */
    @GetMapping("/{orderId}/edit")
    public String edit(@PathVariable Long orderId, Model model) {
        logger.info("Entering ServiceOrderController.edit with orderId={}", orderId);

        try {
            Optional<ServiceOrder> order = serviceOrderService.getServiceOrder(orderId);
            if (order.isEmpty()) {
                logger.info("Leaving ServiceOrderController.edit with missing order");
                return "redirect:/service-orders?missing";
            }

            ServiceOrderUpdateForm updateForm = new ServiceOrderUpdateForm();
            updateForm.setStatus(order.get().getStatus());
            updateForm.setTotalAmount(order.get().getTotalAmount());
            prepareEditForm(model, order.get(), updateForm);
        } catch (DataAccessException exception) {
            logger.info("Leaving ServiceOrderController.edit with database error");
            return "redirect:/service-orders?databaseError";
        }

        logger.info("Leaving ServiceOrderController.edit");
        return "service-orders/edit";
    }

    /**
     * Saves the updated status and total amount.
     *
     * @param orderId order id from the URL
     * @param updateForm form fields from the page
     * @param bindingResult validation results
     * @param model page data sent to the view
     * @return redirect when updated or the form when there are errors
     */
    @PostMapping("/{orderId}")
    public String update(
            @PathVariable Long orderId,
            @Valid @ModelAttribute("serviceOrderUpdateForm") ServiceOrderUpdateForm updateForm,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Entering ServiceOrderController.update with orderId={}", orderId);

        Optional<ServiceOrder> order = serviceOrderService.getServiceOrder(orderId);
        if (order.isEmpty()) {
            logger.info("Leaving ServiceOrderController.update with missing order");
            return "redirect:/service-orders?missing";
        }

        if (bindingResult.hasErrors()) {
            prepareEditForm(model, order.get(), updateForm);
            logger.info("Leaving ServiceOrderController.update with validation errors");
            return "service-orders/edit";
        }

        try {
            serviceOrderService.updateServiceOrder(updateForm, orderId);
        } catch (DataAccessException exception) {
            prepareEditForm(model, order.get(), updateForm);
            model.addAttribute("databaseError", true);
            logger.info("Leaving ServiceOrderController.update with database error");
            return "service-orders/edit";
        }

        logger.info("Leaving ServiceOrderController.update with success");
        return "redirect:/service-orders?updated";
    }

    /**
     * Deletes one service order.
     *
     * @param orderId order id from the URL
     * @return redirect back to the service order list
     */
    @PostMapping("/{orderId}/delete")
    public String delete(@PathVariable Long orderId) {
        logger.info("Entering ServiceOrderController.delete with orderId={}", orderId);

        try {
            serviceOrderService.deleteServiceOrder(orderId);
        } catch (DataAccessException exception) {
            logger.info("Leaving ServiceOrderController.delete with database error");
            return "redirect:/service-orders?databaseError";
        }

        logger.info("Leaving ServiceOrderController.delete with success");
        return "redirect:/service-orders?deleted";
    }

    /**
     * Adds bikes and parts so the user can choose them on the form.
     *
     * @param model page data sent to the view
     * @param serviceOrderForm form object used by the page
     */
    private void prepareCreateForm(Model model, ServiceOrderForm serviceOrderForm) {
        model.addAttribute("pageTitle", "Add Service Order");
        model.addAttribute("serviceOrderForm", serviceOrderForm);

        try {
            model.addAttribute("bikes", bikeService.getAllBikes());
            model.addAttribute("parts", partService.getAllParts());
        } catch (DataAccessException exception) {
            model.addAttribute("bikes", java.util.List.of());
            model.addAttribute("parts", java.util.List.of());
            model.addAttribute("databaseError", true);
        }
    }

    /**
     * Adds the current order and status choices to the edit page.
     *
     * @param model page data sent to the view
     * @param order service order being edited
     * @param updateForm form object used by the page
     */
    private void prepareEditForm(Model model, ServiceOrder order, ServiceOrderUpdateForm updateForm) {
        model.addAttribute("pageTitle", "Update Service Order");
        model.addAttribute("order", order);
        model.addAttribute("serviceOrderUpdateForm", updateForm);
        model.addAttribute("statuses", OrderStatus.values());
    }

    /**
     * Converts the filter text into a status value.
     *
     * @param status status text from the query string
     * @return matching status or null when no valid status is selected
     */
    private OrderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return OrderStatus.valueOf(status);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
