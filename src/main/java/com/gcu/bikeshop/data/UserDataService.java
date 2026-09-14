package com.gcu.bikeshop.data;

import com.gcu.bikeshop.model.User;
import com.gcu.bikeshop.model.UserForm;
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
 * Uses Spring JDBC to access customer data in the users table.
 */
@Repository
public class UserDataService {
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Reads all customers from the users table.
     *
     * @return list of customers
     */
    public List<User> findAll() {
        logger.info("Entering UserDataService.findAll");
        String sql = """
                SELECT user_id, first_name, last_name, email, phone, role
                FROM users
                ORDER BY last_name, first_name
                """;
        List<User> users = jdbcTemplate.query(sql, this::mapUser);
        logger.info("Leaving UserDataService.findAll with {} users", users.size());
        return users;
    }

    /**
     * Finds one customer by their user id.
     *
     * @param userId customer id
     * @return customer when found
     */
    public Optional<User> findById(Long userId) {
        logger.info("Entering UserDataService.findById with userId={}", userId);
        String sql = """
                SELECT user_id, first_name, last_name, email, phone, role
                FROM users
                WHERE user_id = ?
                """;

        try {
            User user = jdbcTemplate.queryForObject(sql, this::mapUser, userId);
            logger.info("Leaving UserDataService.findById with found=true");
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException exception) {
            logger.info("Leaving UserDataService.findById with found=false");
            return Optional.empty();
        }
    }

    /**
     * Inserts a new customer into the users table.
     *
     * @param userForm customer form data
     */
    public void create(UserForm userForm) {
        logger.info("Entering UserDataService.create");
        String sql = """
                INSERT INTO users (first_name, last_name, email, password_hash, phone, role)
                VALUES (?, ?, ?, ?, ?, 'CUSTOMER')
                """;
        jdbcTemplate.update(
                sql,
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getEmail(),
                "temporary-demo-password",
                userForm.getPhone()
        );
        logger.info("Leaving UserDataService.create");
    }

    /**
     * Updates an existing customer in the users table.
     *
     * @param userForm customer form data
     * @param userId customer id
     */
    public void update(UserForm userForm, Long userId) {
        logger.info("Entering UserDataService.update with userId={}", userId);
        String sql = """
                UPDATE users
                SET first_name = ?,
                    last_name = ?,
                    email = ?,
                    phone = ?
                WHERE user_id = ?
                """;
        jdbcTemplate.update(
                sql,
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getEmail(),
                userForm.getPhone(),
                userId
        );
        logger.info("Leaving UserDataService.update with userId={}", userId);
    }

    /**
     * Deletes a customer from the users table.
     *
     * @param userId customer id
     */
    public void delete(Long userId) {
        logger.info("Entering UserDataService.delete with userId={}", userId);
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
        logger.info("Leaving UserDataService.delete with userId={}", userId);
    }

    /**
     * Converts one database row into a User object.
     *
     * @param resultSet database row results
     * @param rowNumber current row number
     * @return mapped user
     * @throws SQLException when a database column cannot be read
     */
    private User mapUser(ResultSet resultSet, int rowNumber) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("phone"),
                resultSet.getString("role")
        );
    }
}
