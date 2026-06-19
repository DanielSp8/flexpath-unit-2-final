package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemDao orderItemDao;

    /**
     *
     * @return all of the order-items in the DB
     */
    @GetMapping
    public List<OrderItem> getOrderItems() {
        return orderItemDao.getOrderItems();
    }

    @GetMapping(path = "/{id}")
    public OrderItem getOrderItemById(@PathVariable int id) {
        try {
            return orderItemDao.getOrderItemById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found",
                    ex
            );
        }
    }


}
