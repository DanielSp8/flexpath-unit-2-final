package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/order-items")
@PreAuthorize("isAuthenticated()")
public class OrderItemController {
    @Autowired
    private OrderItemDao orderItemDao;

    // Get all order-items
    @GetMapping
    public List<OrderItem> getAll() {
        return orderItemDao.getAllOrderItems();
    }

    // Get order-item by its id
    @GetMapping(path = "/{id}")
    public OrderItem getById(@PathVariable int id) {
        OrderItem orderItem = orderItemDao.getById(id);
        if (orderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item was not found.");
        } else {
            return orderItem;
        }
    }

    // Create an order-item
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItemRequest) {
        OrderItem newOrderItem = new OrderItem(
                orderItemRequest.getId(),
                orderItemRequest.getOrderId(),
                orderItemRequest.getProductId(),
                orderItemRequest.getQuantity());
        return orderItemDao.createOrderItem(newOrderItem);
    }

    // Update an order item by its id
    @PutMapping(path="/{id}")
    public OrderItem updateOrderItem(@RequestBody OrderItem orderItemRequest, @PathVariable int id) {
        orderItemRequest.setId(id);
        OrderItem orderItem = new OrderItem(
                orderItemRequest.getId(),
                orderItemRequest.getOrderId(),
                orderItemRequest.getProductId(),
                orderItemRequest.getQuantity()
        );
        OrderItem updatedOrderItem = orderItemDao.updateOrderItem(orderItem);
        if (updatedOrderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        return orderItem;
    }

    // Delete an order item by its id
    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderItemDao.deleteOrder(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item was not found.");
        } else {
            return rowsAffected;
        }
    }
}
