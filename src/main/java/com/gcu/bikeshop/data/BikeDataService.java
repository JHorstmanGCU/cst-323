package com.gcu.bikeshop.data;

import com.gcu.bikeshop.model.Bike;
import com.gcu.bikeshop.model.BikeForm;
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
 * Uses Spring JDBC to access bike data in the bikes table.
 */
@Repository
public class BikeDataService {
    private static final Logger logger = LoggerFactory.getLogger(BikeDataService.class);
    private final JdbcTemplate jdbcTemplate;

    public BikeDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Reads all bikes and their customer names.
     *
     * @return list of bikes
     */
    public List<Bike> findAll() {
        logger.info("Entering BikeDataService.findAll");
        String sql = baseSelect() + """
                ORDER BY u.last_name, u.first_name, b.brand, b.model
                """;
        List<Bike> bikes = jdbcTemplate.query(sql, this::mapBike);
        logger.info("Leaving BikeDataService.findAll with {} bikes", bikes.size());
        return bikes;
    }

    /**
     * Finds one bike by its bike id.
     *
     * @param bikeId bike id
     * @return bike when found
     */
    public Optional<Bike> findById(Long bikeId) {
        logger.info("Entering BikeDataService.findById with bikeId={}", bikeId);
        String sql = baseSelect() + "WHERE b.bike_id = ?";

        try {
            Bike bike = jdbcTemplate.queryForObject(sql, this::mapBike, bikeId);
            logger.info("Leaving BikeDataService.findById with found=true");
            return Optional.ofNullable(bike);
        } catch (EmptyResultDataAccessException exception) {
            logger.info("Leaving BikeDataService.findById with found=false");
            return Optional.empty();
        }
    }

    /**
     * Inserts a new bike into the bikes table.
     *
     * @param bikeForm bike form data
     */
    public void create(BikeForm bikeForm) {
        logger.info("Entering BikeDataService.create for brand={}, model={}", bikeForm.getBrand(), bikeForm.getModel());
        String sql = """
                INSERT INTO bikes (user_id, brand, model, serial_number)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                bikeForm.getUserId(),
                bikeForm.getBrand(),
                bikeForm.getModel(),
                bikeForm.getSerialNumber()
        );
        logger.info("Leaving BikeDataService.create for brand={}, model={}", bikeForm.getBrand(), bikeForm.getModel());
    }

    /**
     * Updates an existing bike in the bikes table.
     *
     * @param bikeForm bike form data
     * @param bikeId bike id
     */
    public void update(BikeForm bikeForm, Long bikeId) {
        logger.info("Entering BikeDataService.update with bikeId={}", bikeId);
        String sql = """
                UPDATE bikes
                SET user_id = ?,
                    brand = ?,
                    model = ?,
                    serial_number = ?
                WHERE bike_id = ?
                """;
        jdbcTemplate.update(
                sql,
                bikeForm.getUserId(),
                bikeForm.getBrand(),
                bikeForm.getModel(),
                bikeForm.getSerialNumber(),
                bikeId
        );
        logger.info("Leaving BikeDataService.update with bikeId={}", bikeId);
    }

    /**
     * Deletes a bike from the bikes table.
     *
     * @param bikeId bike id
     */
    public void delete(Long bikeId) {
        logger.info("Entering BikeDataService.delete with bikeId={}", bikeId);
        String sql = "DELETE FROM bikes WHERE bike_id = ?";
        jdbcTemplate.update(sql, bikeId);
        logger.info("Leaving BikeDataService.delete with bikeId={}", bikeId);
    }

    /**
     * Keeps the shared bike SELECT statement in one place.
     *
     * @return base bike query
     */
    private String baseSelect() {
        return """
                SELECT
                    b.bike_id,
                    b.user_id,
                    CONCAT(u.first_name, ' ', u.last_name) AS customer_name,
                    b.brand,
                    b.model,
                    b.serial_number
                FROM bikes b
                JOIN users u ON u.user_id = b.user_id
                """;
    }

    /**
     * Converts one database row into a Bike object.
     *
     * @param resultSet database row results
     * @param rowNumber current row number
     * @return mapped bike
     * @throws SQLException when a database column cannot be read
     */
    private Bike mapBike(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Bike(
                resultSet.getLong("bike_id"),
                resultSet.getLong("user_id"),
                resultSet.getString("customer_name"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getString("serial_number")
        );
    }
}
