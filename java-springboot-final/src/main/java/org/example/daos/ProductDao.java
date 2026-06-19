package org.example.daos;


import org.example.exceptions.DaoException;
import org.example.models.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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


    private Product mapToProducts(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }

}
