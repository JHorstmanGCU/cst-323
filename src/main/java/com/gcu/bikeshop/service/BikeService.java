package com.gcu.bikeshop.service;

import com.gcu.bikeshop.data.BikeDataService;
import com.gcu.bikeshop.model.Bike;
import com.gcu.bikeshop.model.BikeForm;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handles bike business logic.
 */
@Service
public class BikeService {
    private static final Logger logger = LoggerFactory.getLogger(BikeService.class);
    private final BikeDataService bikeDataService;

    public BikeService(BikeDataService bikeDataService) {
        this.bikeDataService = bikeDataService;
    }

    /**
     * Gets all bikes from the data service.
     *
     * @return list of bikes
     */
    public List<Bike> getAllBikes() {
        logger.info("Entering BikeService.getAllBikes");
        List<Bike> bikes = bikeDataService.findAll();
        logger.info("Leaving BikeService.getAllBikes with {} bikes", bikes.size());
        return bikes;
    }

    /**
     * Gets one bike from the data service.
     *
     * @param bikeId bike id
     * @return bike when found
     */
    public Optional<Bike> getBike(Long bikeId) {
        logger.info("Entering BikeService.getBike with bikeId={}", bikeId);
        Optional<Bike> bike = bikeDataService.findById(bikeId);
        logger.info("Leaving BikeService.getBike with found={}", bike.isPresent());
        return bike;
    }

    /**
     * Sends the new bike form to the data service.
     *
     * @param bikeForm bike form data
     */
    public void createBike(BikeForm bikeForm) {
        logger.info("Entering BikeService.createBike for brand={}, model={}", bikeForm.getBrand(), bikeForm.getModel());
        bikeDataService.create(bikeForm);
        logger.info("Leaving BikeService.createBike for brand={}, model={}", bikeForm.getBrand(), bikeForm.getModel());
    }

    /**
     * Sends the bike update to the data service.
     *
     * @param bikeForm bike form data
     * @param bikeId bike id
     */
    public void updateBike(BikeForm bikeForm, Long bikeId) {
        logger.info("Entering BikeService.updateBike with bikeId={}", bikeId);
        bikeDataService.update(bikeForm, bikeId);
        logger.info("Leaving BikeService.updateBike with bikeId={}", bikeId);
    }

    /**
     * Sends the bike delete request to the data service.
     *
     * @param bikeId bike id
     */
    public void deleteBike(Long bikeId) {
        logger.info("Entering BikeService.deleteBike with bikeId={}", bikeId);
        bikeDataService.delete(bikeId);
        logger.info("Leaving BikeService.deleteBike with bikeId={}", bikeId);
    }
}
