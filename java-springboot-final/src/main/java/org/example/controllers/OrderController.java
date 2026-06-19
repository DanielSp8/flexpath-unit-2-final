package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     *
     * @param id, this variable passes the id to update to
     * @param order this is the object to update
     * @return the updated order or the error (if it's not found)
     */

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

    /**
     *
     * @param id, the specific id to attempt to delete
     * @return: Check if the integer removedNum shows 0, signifying nothing was deleted,
     *           and thus, not found.
     *          Or return: (1) if it's deleted.
     */
    @DeleteMapping(path = "/{id}")
    public int delete(@PathVariable int id) {
        int removedNum = orderDao.deleteOrder(id);
        if (removedNum == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        } else {
            return removedNum;
        }
    }

}
