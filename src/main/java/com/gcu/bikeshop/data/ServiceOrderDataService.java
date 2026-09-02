package com.gcu.bikeshop.data;

import com.gcu.bikeshop.model.OrderStatus;
import com.gcu.bikeshop.model.ServiceOrder;
import com.gcu.bikeshop.model.ServiceOrderForm;
import com.gcu.bikeshop.model.ServiceOrderUpdateForm;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Uses Spring JDBC to access service order data in the service_orders table.
 */
@Repository
public class ServiceOrderDataService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceOrderDataService.class);
    private final JdbcTemplate jdbcTemplate;

    public ServiceOrderDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Reads all service orders with customer, bike, and part details.
     *
     * @return list of service orders
     */
    public List<ServiceOrder> findAll() {
        logger.info("Entering ServiceOrderDataService.findAll");
        String sql = baseSelect() + """
                ORDER BY FIELD(o.status, 'NOT_STARTED', 'IN_PROGRESS', 'READY'), o.order_id DESC
                """;
        List<ServiceOrder> orders = jdbcTemplate.query(sql, this::mapServiceOrder);
        logger.info("Leaving ServiceOrderDataService.findAll with {} orders", orders.size());
        return orders;
    }

    /**
     * Reads service orders that match one status.
     *
     * @param status selected order status
     * @return list of matching service orders
     */
    public List<ServiceOrder> findByStatus(OrderStatus status) {
        logger.info("Entering ServiceOrderDataService.findByStatus with status={}", status);
        String sql = baseSelect() + """
                WHERE o.status = ?
                ORDER BY o.order_id DESC
                """;
        List<ServiceOrder> orders = jdbcTemplate.query(sql, this::mapServiceOrder, status.name());
        logger.info("Leaving ServiceOrderDataService.findByStatus with {} orders", orders.size());
        return orders;
    }

    /**
     * Finds one service order by its order id.
     *
     * @param orderId service order id
     * @return service order when found
     */
    public Optional<ServiceOrder> findById(Long orderId) {
        logger.info("Entering ServiceOrderDataService.findById with orderId={}", orderId);
        String sql = baseSelect() + "WHERE o.order_id = ?";

        try {
            ServiceOrder order = jdbcTemplate.queryForObject(sql, this::mapServiceOrder, orderId);
            logger.info("Leaving ServiceOrderDataService.findById with found=true");
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException exception) {
            logger.info("Leaving ServiceOrderDataService.findById with found=false");
            return Optional.empty();
        }
    }

    /**
     * Inserts a new service order into the database.
     *
     * @param serviceOrderForm service order form data
     */
    public void create(ServiceOrderForm serviceOrderForm) {
        logger.info("Entering ServiceOrderDataService.create for bikeId={}", serviceOrderForm.getBikeId());
        String sql = """
                INSERT INTO service_orders
                    (bike_id, part_id, service_type, problem_description, dropoff_date, total_amount)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                serviceOrderForm.getBikeId(),
                serviceOrderForm.getPartId(),
                serviceOrderForm.getServiceType(),
                serviceOrderForm.getProblemDescription(),
                toSqlDate(serviceOrderForm.getDropoffDate()),
                serviceOrderForm.getTotalAmount()
        );
        logger.info("Leaving ServiceOrderDataService.create for bikeId={}", serviceOrderForm.getBikeId());
    }

    /**
     * Updates the status and total amount for an order.
     *
     * @param updateForm service order update form data
     * @param orderId service order id
     */
    public void update(ServiceOrderUpdateForm updateForm, Long orderId) {
        logger.info("Entering ServiceOrderDataService.update for orderId={}", orderId);
        String sql = """
                UPDATE service_orders
                SET status = ?,
                    total_amount = ?
                WHERE order_id = ?
                """;
        jdbcTemplate.update(
                sql,
                updateForm.getStatus().name(),
                updateForm.getTotalAmount(),
                orderId
        );
        logger.info("Leaving ServiceOrderDataService.update for orderId={}", orderId);
    }

    /**
     * Deletes a service order from the database.
     *
     * @param orderId service order id
     */
    public void delete(Long orderId) {
        logger.info("Entering ServiceOrderDataService.delete with orderId={}", orderId);
        String sql = "DELETE FROM service_orders WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
        logger.info("Leaving ServiceOrderDataService.delete with orderId={}", orderId);
    }

    /**
     * Keeps the shared SELECT statement in one place.
     *
     * @return base service order query
     */
    private String baseSelect() {
        return """
                SELECT
                    o.order_id,
                    o.bike_id,
                    o.part_id,
                    CONCAT(u.first_name, ' ', u.last_name) AS customer_name,
                    CONCAT(b.brand, ' ', b.model) AS bike_name,
                    p.part_name,
                    o.service_type,
                    o.problem_description,
                    o.status,
                    o.dropoff_date,
                    o.total_amount
                FROM service_orders o
                JOIN bikes b ON b.bike_id = o.bike_id
                JOIN users u ON u.user_id = b.user_id
                LEFT JOIN parts p ON p.part_id = o.part_id
                """;
    }

    /**
     * Converts one database row into a ServiceOrder object.
     *
     * @param resultSet database row results
     * @param rowNumber current row number
     * @return mapped service order
     * @throws SQLException when a database column cannot be read
     */
    private ServiceOrder mapServiceOrder(ResultSet resultSet, int rowNumber) throws SQLException {
        return new ServiceOrder(
                resultSet.getLong("order_id"),
                resultSet.getLong("bike_id"),
                nullableLong(resultSet, "part_id"),
                resultSet.getString("customer_name"),
                resultSet.getString("bike_name"),
                resultSet.getString("part_name"),
                resultSet.getString("service_type"),
                resultSet.getString("problem_description"),
                OrderStatus.valueOf(resultSet.getString("status")),
                toLocalDate(resultSet.getDate("dropoff_date")),
                resultSet.getBigDecimal("total_amount")
        );
    }

    /**
     * Returns null when an optional id is missing.
     *
     * @param resultSet database row results
     * @param columnName column name to read
     * @return nullable id value
     * @throws SQLException when a database column cannot be read
     */
    private Long nullableLong(ResultSet resultSet, String columnName) throws SQLException {
        long value = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : value;
    }

    /**
     * Converts a Java date into a SQL date for MySQL.
     *
     * @param date Java local date
     * @return SQL date or null
     */
    private Date toSqlDate(LocalDate date) {
        return date == null ? null : Date.valueOf(date);
    }

    /**
     * Converts a SQL date back into a Java date.
     *
     * @param date SQL date
     * @return Java local date or null
     */
    private LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
