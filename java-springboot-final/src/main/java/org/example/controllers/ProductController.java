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

    /**
     *
     * @param id
     * @return a product by its id
     */
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

    /**
     *
     * @param product
     * @return The product that was created in the DB
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productDao.createProduct(product);
    }

    /**
     *
     * @param id
     * @param product
     * @return Either the updated product info
     *          or a ResponseStatusException (404) error message
     */
    @PutMapping(path = "/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            return productDao.updateProduct(id, product);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found",
                    ex
            );
        }
    }

    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int removedNum = productDao.deleteProduct(id);
        if (removedNum == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found");
        } else {
            return removedNum;
        }
    }

}
