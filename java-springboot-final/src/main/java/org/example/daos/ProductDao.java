package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;


    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Gets all products
    public List<Product> getAll() {
        return jdbcTemplate.query("SELECT * FROM products;", this::mapToProduct);
    }


    // Get a product by id
    public Product getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM products WHERE id = ?;", this::mapToProduct, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Create a new product
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?);";
        try {
            jdbcTemplate.update(sql, product.getName(), product.getPrice());
            return product;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create product.");
        }
    }

    // Update a product
    public Product updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getId());

        if (rowsAffected == 0) {
            return null;
        }

        return product;
    }

    // Delete a product by its id
    public int deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }




    // Maps to a Product object.
    private Product mapToProduct(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        return new Product(
            id,
            name,
            price
        );
    }


}
