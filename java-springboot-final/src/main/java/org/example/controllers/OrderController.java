package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    @Autowired
    private OrderDao orderDao;

    // Get all orders
    @GetMapping
    public List<Order> listOrders(@RequestParam(required = false) String username) {
        if (username != null) {
            return orderDao.getOrdersByUsername(username);
        } else {
            return orderDao.getAllOrders();
        }
    }

    // Get order by id
    @GetMapping(path = "/{id}")
    public Order getById(@PathVariable int id) {
        Order order = orderDao.getById(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order was not found.");
        } else {
            return order;
        }
    }

    // Create an order
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order createOrder(@RequestBody Order orderRequest, Principal principal) {
        Order order = new Order(6, principal.getName());
        return orderDao.createOrder(order);
    }

    // Update an order by its id
    @PutMapping(path = "/{id}")
    public Order updateOrder(@RequestBody Order orderRequest, @PathVariable int id) {
        Order updatedOrder = new Order(id, orderRequest.getUsername());
        Order updateTheOrder = orderDao.updateOrder(updatedOrder);

        if (updateTheOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        } else {
            return updatedOrder;
        }
    }

    // Delete an order by its id
    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderDao.deleteOrder(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order was not found.");
        } else {
            return rowsAffected;
        }
    }
}
