package org.example.controllers;

import org.example.daos.ProductDao;
import org.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
@PreAuthorize("isAuthenticated()")
public class ProductController {
    @Autowired
    private ProductDao productDao;

    // Gets all products
    @GetMapping
    public List<Product> getAll() {
        return productDao.getAll();
    }

    // Get product by id
    @GetMapping(path = "/{id}")
    public Product getById(@PathVariable int id) {
        Product product = productDao.getById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        } else {
            return product;
        }
    }

    // Create a product
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product createProduct(@RequestBody Product productRequest) {
        Product newProduct = new Product(
                6,
                productRequest.getName(),
                productRequest.getPrice()
        );
        return productDao.createProduct(newProduct);
    }

    // Update a product by its id
    @PutMapping(path = "/{id}")
    public Product updateProduct(@RequestBody Product productRequest, @PathVariable int id) {
        Product product = new Product(id, productRequest.getName(), productRequest.getPrice());
        Product updatedProduct = productDao.updateProduct(product);
        if (updatedProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found.");
        }
        return updatedProduct;
    }

    // Delete a product by its id
    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = productDao.deleteProduct(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product was not found.");
        } else {
            return rowsAffected;
        }
    }
}
