package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Return all orders
     */
    public List<Order> getOrders() {
        return jdbcTemplate.query("SELECT * FROM orders;", this::mapToOrders);
    }

    /**
     * Return an order by its ID
     */
    public Order getOrderById(int orderId) {
        return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?;", this::mapToOrders, orderId);
    }

    /**
     *
     * @param order
     * Create an order with the order object
     * @return the order with the updated id key
     */
    public Order createOrder(Order order) {
        try {
            String sql = "INSERT INTO orders (username) VALUES (?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, order.getUsername());
                        return ps;
                    }, keyHolder);
            order.setId(keyHolder.getKey().intValue());
            return order;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create new order.");
        }
    }

    /**
     * Update an order by its ID
     */
    public Order updateOrder(int id, Order order) {
        String sql = "UPDATE orders SET username = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, order.getUsername(), id);
        if (rowsAffected == 0) {
            throw new DaoException("Failed to update order.");
        } else {
            return getOrderById(id);
        }
    }


    /**
     *  Delete an order by its ID
     */
    public int deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?;";
        return jdbcTemplate.update(sql, orderId);
    }


    /**
     *
     * @param resultSet
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    private Order mapToOrders(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Order(
            id,
            resultSet.getString("username")
        );
    }
}
