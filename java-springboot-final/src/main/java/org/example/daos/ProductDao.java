package org.example.daos;


import org.example.exceptions.DaoException;
import org.example.models.Product;
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

    /**
     *
     * @param productId
     * @return The product by its id from the DB
     */
    public Product getProductById(int productId) {
        return jdbcTemplate.queryForObject("SELECT * FROM products WHERE id = ?;", this::mapToProducts, productId);
    }

    /**
     *
     * @param product
     * @return The product object, which was created in the DB
     */
    public Product createProduct(Product product) {
        try {
            String sql = "INSERT INTO products (name, price) VALUES (?, ?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setBigDecimal(2, product.getPrice());
                return ps;
            }, keyHolder);
            product.setId(keyHolder.getKey().intValue());
            return product;
        } catch(EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create new product.");
        }
    }

    /**
     *
     * @param id
     * @param product
     * @return
     */
    public Product updateProduct(int id,  Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, product.getName(), product.getPrice(), id);
        if (rowsAffected == 0) {
            throw new DaoException("Failed to update product.");
        } else {
            return getProductById(id);
        }
    }

    /**
     *
     * @param id, the primary key to reference the product
     * @return the info from the DB delete attempt
     */
    public int deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }


    /**
     *
     * @param resultSet
     * @param rowNumber
     * @return a new product object, constructed from the columns retrieved from the DB rowNumber(s)
     * @throws SQLException
     */
    private Product mapToProducts(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }





}
