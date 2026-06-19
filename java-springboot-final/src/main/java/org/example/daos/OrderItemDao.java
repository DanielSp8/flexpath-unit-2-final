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
     * @param orderItemId, the primary id of the order item to GET from the DB
     * @return an order item by its id
     */
    public OrderItem getOrderItemById(int orderItemId) {
        return jdbcTemplate.queryForObject("SELECT * FROM order_items WHERE id = ?;", this::mapToOrderItems, orderItemId);
    }

    /**
     *
     * @param orderItem, the object to create in the DB
     * @return the orderItem that was created in the DB,
     *   or an error message if it fails to create it
     */
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

    /**
     *
     * @param id, references the primary key to update in the DB
     * @param orderItem, the object to update in the DB
     * @return throw an error message (if 0 rows are affected, indicating that the DB was not updated,
     *          or return the DB order item by its primary id
     */
    public OrderItem updateOrderItem(int id, OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), id);
        if (rowsAffected == 0) {
            throw new DaoException("Failed to update order item.");
        } else {
            return getOrderItemById(id);
        }
    }

    public int deleteOrderItem(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
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
