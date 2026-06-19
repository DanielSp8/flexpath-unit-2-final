package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.OrderItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     *
     * @return all order items
     */
    public List<OrderItem> getOrderItems() {
        return jdbcTemplate.query("SELECT * FROM order_items;", this::mapToOrderItems);
    }

    /**
     *
     * @param orderItemId
     * @return an order item by its id
     */
    public OrderItem getOrderItemById(int orderItemId) {
        return jdbcTemplate.queryForObject("SELECT * FROM order_items WHERE id = ?;", this::mapToOrderItems, orderItemId);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        try {
            String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, orderItem.getOrderId());
                ps.setInt(2, orderItem.getProductId());
                ps.setInt(3, orderItem.getQuantity());
                return ps;
            }, keyHolder);
            orderItem.setId(keyHolder.getKey().intValue());
            return orderItem;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create new order item.");
        }
    }


    private OrderItem mapToOrderItems(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new OrderItem(
                id,
                resultSet.getInt("order_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity")
        );
    }
}
