package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return jdbcTemplate.query("SELECT * FROM orders;", this::mapToOrder);
    }

    // Get an order by its id
    public Order getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?;", this::mapToOrder, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Get an order by its username
    public List<Order> getOrdersByUsername(String username) {
        try {
            return jdbcTemplate.query("SELECT * FROM orders WHERE username = ?;", this::mapToOrder, username);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Create a new order
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (username) VALUES (?);";
        try {
            jdbcTemplate.update(sql, order.getUsername());
            return order;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create order.");
        }
    }

    // Update an order
    public Order updateOrder(Order order) {
        boolean nameExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE username = ?);",
                Boolean.class,
                order.getUsername()));

        if (!nameExists) {
            throw new DataIntegrityViolationException(("Username does not exist."));
        }

        String sql = "UPDATE orders SET username = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, order.getUsername(), order.getId());

        if (rowsAffected == 0) {
            return null;
        } else {
            return order;
        }
    }

    // Delete an order by its id
    public int deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }


    private Order mapToOrder(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        return new Order(
                id,
                username
        );
    }
}
