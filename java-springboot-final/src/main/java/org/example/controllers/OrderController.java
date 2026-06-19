package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")

public class OrderController {


    /**
     *  The order data access object
     */
    @Autowired
    private OrderDao orderDao;

    /**
     *
     * @return all the orders in the DB
     */
    @GetMapping
    public List<Order> getAll() {
        return orderDao.getOrders();
    }


    /**
     *
     * @param id
     * @return a single order by its id from the DB
     */
    @GetMapping(path = "/{id}")
    public Order get(@PathVariable int id) {
        try {
            return orderDao.getOrderById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found",
                    ex
            );
        }
    }

    /**
     *
     * @param order
     * @return the created order object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderDao.createOrder(order);
    }

    @PutMapping(path = "/{id}")
    public Order update(@PathVariable int id, @RequestBody Order order) {
        try {
            return orderDao.updateOrder(id, order);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found",
                    ex);
        }
    }




}
