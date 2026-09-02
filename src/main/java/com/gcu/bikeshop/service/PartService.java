package com.gcu.bikeshop.service;

import com.gcu.bikeshop.data.PartDataService;
import com.gcu.bikeshop.model.Part;
import com.gcu.bikeshop.model.PartForm;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handles part inventory business logic.
 */
@Service
public class PartService {
    private static final Logger logger = LoggerFactory.getLogger(PartService.class);
    private final PartDataService partDataService;

    public PartService(PartDataService partDataService) {
        this.partDataService = partDataService;
    }

    /**
     * Gets all parts from the data service.
     *
     * @return list of parts
     */
    public List<Part> getAllParts() {
        logger.info("Entering PartService.getAllParts");
        List<Part> parts = partDataService.findAll();
        logger.info("Leaving PartService.getAllParts with {} parts", parts.size());
        return parts;
    }

    /**
     * Gets one part from the data service.
     *
     * @param partId part id
     * @return part when found
     */
    public Optional<Part> getPart(Long partId) {
        logger.info("Entering PartService.getPart with partId={}", partId);
        Optional<Part> part = partDataService.findById(partId);
        logger.info("Leaving PartService.getPart with found={}", part.isPresent());
        return part;
    }

    /**
     * Sends the new part form to the data service.
     *
     * @param partForm part form data
     */
    public void createPart(PartForm partForm) {
        logger.info("Entering PartService.createPart for partName={}", partForm.getPartName());
        partDataService.create(partForm);
        logger.info("Leaving PartService.createPart for partName={}", partForm.getPartName());
    }

    /**
     * Sends the part update to the data service.
     *
     * @param partForm part form data
     * @param partId part id
     */
    public void updatePart(PartForm partForm, Long partId) {
        logger.info("Entering PartService.updatePart with partId={}", partId);
        partDataService.update(partForm, partId);
        logger.info("Leaving PartService.updatePart with partId={}", partId);
    }

    /**
     * Sends the part delete request to the data service.
     *
     * @param partId part id
     */
    public void deletePart(Long partId) {
        logger.info("Entering PartService.deletePart with partId={}", partId);
        partDataService.delete(partId);
        logger.info("Leaving PartService.deletePart with partId={}", partId);
    }
}
