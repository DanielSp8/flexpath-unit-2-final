package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.OrderItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Get all order items
    public List<OrderItem> getAllOrderItems() {
        return jdbcTemplate.query("SELECT * FROM order_items;", this::mapToOrderItems);
    }

    // Get an order item by its id
    public OrderItem getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM order_items WHERE id = ?;", this::mapToOrderItems, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Create a new order item
    public OrderItem createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?);";
        try {
            jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());
            return orderItem;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create a new order item.");
        }
    }

    // Update an order item
    public OrderItem updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getId());

        if (rowsAffected == 0) {
            return null;
        }

        return orderItem;
    }

    // Delete an order item by its id
    public int deleteOrder(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    private OrderItem mapToOrderItems(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        int orderId = resultSet.getInt("order_id");
        int productId = resultSet.getInt("product_id");
        int quantity = resultSet.getInt("quantity");
        return new OrderItem(
                id,
                orderId,
                productId,
                quantity
        );
    }


}
