package com.gcu.bikeshop.data;

import com.gcu.bikeshop.model.Part;
import com.gcu.bikeshop.model.PartForm;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Uses Spring JDBC to access part inventory data in the parts table.
 */
@Repository
public class PartDataService {
    private static final Logger logger = LoggerFactory.getLogger(PartDataService.class);
    private final JdbcTemplate jdbcTemplate;

    public PartDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Reads all parts from inventory.
     *
     * @return list of parts
     */
    public List<Part> findAll() {
        logger.info("Entering PartDataService.findAll");
        String sql = """
                SELECT part_id, part_name, part_number, price, quantity
                FROM parts
                ORDER BY part_name
                """;
        List<Part> parts = jdbcTemplate.query(sql, this::mapPart);
        logger.info("Leaving PartDataService.findAll with {} parts", parts.size());
        return parts;
    }

    /**
     * Finds one part by its part id.
     *
     * @param partId part id
     * @return part when found
     */
    public Optional<Part> findById(Long partId) {
        logger.info("Entering PartDataService.findById with partId={}", partId);
        String sql = """
                SELECT part_id, part_name, part_number, price, quantity
                FROM parts
                WHERE part_id = ?
                """;

        try {
            Part part = jdbcTemplate.queryForObject(sql, this::mapPart, partId);
            logger.info("Leaving PartDataService.findById with found=true");
            return Optional.ofNullable(part);
        } catch (EmptyResultDataAccessException exception) {
            logger.info("Leaving PartDataService.findById with found=false");
            return Optional.empty();
        }
    }

    /**
     * Inserts a new part into the parts table.
     *
     * @param partForm part form data
     */
    public void create(PartForm partForm) {
        logger.info("Entering PartDataService.create for partName={}", partForm.getPartName());
        String sql = """
                INSERT INTO parts (part_name, part_number, price, quantity)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                partForm.getPartName(),
                partForm.getPartNumber(),
                partForm.getPrice(),
                partForm.getQuantity()
        );
        logger.info("Leaving PartDataService.create for partName={}", partForm.getPartName());
    }

    /**
     * Updates an existing part in the parts table.
     *
     * @param partForm part form data
     * @param partId part id
     */
    public void update(PartForm partForm, Long partId) {
        logger.info("Entering PartDataService.update with partId={}", partId);
        String sql = """
                UPDATE parts
                SET part_name = ?,
                    part_number = ?,
                    price = ?,
                    quantity = ?
                WHERE part_id = ?
                """;
        jdbcTemplate.update(
                sql,
                partForm.getPartName(),
                partForm.getPartNumber(),
                partForm.getPrice(),
                partForm.getQuantity(),
                partId
        );
        logger.info("Leaving PartDataService.update with partId={}", partId);
    }

    /**
     * Deletes a part from the parts table.
     *
     * @param partId part id
     */
    public void delete(Long partId) {
        logger.info("Entering PartDataService.delete with partId={}", partId);
        String sql = "DELETE FROM parts WHERE part_id = ?";
        jdbcTemplate.update(sql, partId);
        logger.info("Leaving PartDataService.delete with partId={}", partId);
    }

    /**
     * Converts one database row into a Part object.
     *
     * @param resultSet database row results
     * @param rowNumber current row number
     * @return mapped part
     * @throws SQLException when a database column cannot be read
     */
    private Part mapPart(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Part(
                resultSet.getLong("part_id"),
                resultSet.getString("part_name"),
                resultSet.getString("part_number"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("quantity")
        );
    }
}
