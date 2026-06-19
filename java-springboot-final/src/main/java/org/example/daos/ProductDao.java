package org.example.daos;


import org.example.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     *
     * @return A list of all the products
     */
    public List<Product> getProducts() {
        return jdbcTemplate.query("SELECT * FROM products;", this::mapToProducts);
    }

    public Product getProductById(int productId) {
        return jdbcTemplate.queryForObject("SELECT * FROM products WHERE id = ?;", this::mapToProducts, productId);
    }



    private Product mapToProducts(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }

}
