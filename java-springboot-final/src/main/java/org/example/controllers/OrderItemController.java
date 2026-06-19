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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemDao.createOrderItem(orderItem);
    }

    @PutMapping(path = "/{id}")
    public OrderItem updateOrderItem(@PathVariable int id, @RequestBody OrderItem orderItem) {
        try {
            return orderItemDao.updateOrderItem(id, orderItem);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found",
                    ex
            );
        }
    }

    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int removedNum = orderItemDao.deleteOrderItem(id);
        if (removedNum == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found");
        } else {
            return removedNum;
        }
    }


}
