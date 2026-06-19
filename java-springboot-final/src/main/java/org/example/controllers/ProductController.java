package org.example.controllers;

import org.example.daos.ProductDao;
import org.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/products")

public class ProductController {

    @Autowired
    private ProductDao productDao;

    /**
     *
     * @return A list of all products
     */
    @GetMapping
    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    @GetMapping(path = "/{id}")
    public Product getProductById(@PathVariable int id) {
        try {
            return productDao.getProductById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found",
                    ex
            );
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productDao.createProduct(product);
    }


}
