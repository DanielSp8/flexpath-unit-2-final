package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        return orderDao.getOrderById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderDao.createOrder(order);
    }



}
