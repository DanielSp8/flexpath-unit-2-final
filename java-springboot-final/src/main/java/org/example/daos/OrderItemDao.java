package org.example.daos;

import org.example.models.OrderItem;
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

    /**
     *
     * @return all order-items
     */
    public List<OrderItem> getOrderItems() {
        return jdbcTemplate.query("SELECT * FROM order_items;", this::mapToOrderItems);
    }

    public OrderItem getOrderItemById(int orderItemId) {
        return jdbcTemplate.queryForObject("SELECT * FROM order_items WHERE id = ?;", this::mapToOrderItems, orderItemId);
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
