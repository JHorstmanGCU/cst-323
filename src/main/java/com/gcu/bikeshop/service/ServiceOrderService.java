package com.gcu.bikeshop.service;

import com.gcu.bikeshop.data.ServiceOrderDataService;
import com.gcu.bikeshop.model.OrderStatus;
import com.gcu.bikeshop.model.ServiceOrder;
import com.gcu.bikeshop.model.ServiceOrderForm;
import com.gcu.bikeshop.model.ServiceOrderUpdateForm;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handles service order business logic.
 */
@Service
public class ServiceOrderService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderService.class);
    private final ServiceOrderDataService serviceOrderDataService;

    public ServiceOrderService(ServiceOrderDataService serviceOrderDataService) {
        this.serviceOrderDataService = serviceOrderDataService;
    }

    /**
     * Gets all service orders from the data service.
     *
     * @return list of service orders
     */
    public List<ServiceOrder> getAllServiceOrders() {
        logger.info("Entering ServiceOrderService.getAllServiceOrders");
        List<ServiceOrder> orders = serviceOrderDataService.findAll();
        logger.info("Leaving ServiceOrderService.getAllServiceOrders with {} orders", orders.size());
        return orders;
    }

    /**
     * Gets service orders that match one status.
     *
     * @param status selected order status
     * @return list of matching service orders
     */
    public List<ServiceOrder> getServiceOrdersByStatus(OrderStatus status) {
        logger.info("Entering ServiceOrderService.getServiceOrdersByStatus with status={}", status);
        List<ServiceOrder> orders = serviceOrderDataService.findByStatus(status);
        logger.info("Leaving ServiceOrderService.getServiceOrdersByStatus with {} orders", orders.size());
        return orders;
    }

    /**
     * Gets one service order by its id.
     *
     * @param orderId service order id
     * @return service order when found
     */
    public Optional<ServiceOrder> getServiceOrder(Long orderId) {
        logger.info("Entering ServiceOrderService.getServiceOrder with orderId={}", orderId);
        Optional<ServiceOrder> order = serviceOrderDataService.findById(orderId);
        logger.info("Leaving ServiceOrderService.getServiceOrder with found={}", order.isPresent());
        return order;
    }

    /**
     * Sends the new service order form to the data service.
     *
     * @param serviceOrderForm service order form data
     */
    public void createServiceOrder(ServiceOrderForm serviceOrderForm) {
        logger.info("Entering ServiceOrderService.createServiceOrder for bikeId={}", serviceOrderForm.getBikeId());
        serviceOrderDataService.create(serviceOrderForm);
        logger.info("Leaving ServiceOrderService.createServiceOrder for bikeId={}", serviceOrderForm.getBikeId());
    }

    /**
     * Sends the service order update to the data service.
     *
     * @param updateForm service order update form data
     * @param orderId service order id
     */
    public void updateServiceOrder(ServiceOrderUpdateForm updateForm, Long orderId) {
        logger.info("Entering ServiceOrderService.updateServiceOrder for orderId={}", orderId);
        serviceOrderDataService.update(updateForm, orderId);
        logger.info("Leaving ServiceOrderService.updateServiceOrder for orderId={}", orderId);
    }

    /**
     * Sends the service order delete request to the data service.
     *
     * @param orderId service order id
     */
    public void deleteServiceOrder(Long orderId) {
        logger.info("Entering ServiceOrderService.deleteServiceOrder for orderId={}", orderId);
        serviceOrderDataService.delete(orderId);
        logger.info("Leaving ServiceOrderService.deleteServiceOrder for orderId={}", orderId);
    }
}
