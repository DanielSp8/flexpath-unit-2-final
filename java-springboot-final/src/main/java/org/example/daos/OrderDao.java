package org.example.daos;

import org.apache.ibatis.jdbc.SQL;
import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.springframework.dao.DataAccessException;
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
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?;", this::mapToOrders, orderId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * POST/Create a new order
     */
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, user_id) VALUES (?, ?);";
        try {
            jdbcTemplate.update(sql, order.getId(), order.getUsername());
            return order;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create new order.");
        }
    }

    /**
     * Update an order by its ID
     */
    public String updateOrder(Order order) {
        String sql = "UPDATE orders SET order_id = ?, user_id = ? WHERE order_id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, order.getId(), order.getUsername(), order.getId());
        if (rowsAffected == 0) {
            throw new DaoException("Failed to update order.");
        } else {
            return rowsAffected + " row updated with information.";
        }
    }


    /**
     *  Delete an order by its ID
     */
    public int deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?;";
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
