package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemDao orderItemDao;

    @GetMapping
    public List<OrderItem> getOrderItems() {
        return orderItemDao.getOrderItems();
    }
}
