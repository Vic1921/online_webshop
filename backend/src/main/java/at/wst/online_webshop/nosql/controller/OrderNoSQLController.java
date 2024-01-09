package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.OrderNoSqlDTO;
import at.wst.online_webshop.nosql.request.PlaceOrderRequestNoSQL;
import at.wst.online_webshop.nosql.services.OrderNoSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/orders")
public class OrderNoSQLController {

    private static final Logger logger = LoggerFactory.getLogger(OrderNoSQLController.class);
    @Autowired
    private OrderNoSQLService orderNoSQLService;

    @GetMapping("/={customerId}")
    public ResponseEntity<List<OrderNoSqlDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        logger.info("ORDERS ARE BEING FETCHED FROM NOSQL");
        List<OrderNoSqlDTO> orders = orderNoSQLService.getOrdersByCustomerId(customerId);

        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderNoSqlDTO> getOrderById(@PathVariable String id) {
        try {
            OrderNoSqlDTO order = orderNoSQLService.getOrderById(id);
            logger.info("THIS IS THE ORDER");
            logger.info(order.toString());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/order-details")
    public ResponseEntity<OrderNoSqlDTO> getOrderByProductAndCustomer(@RequestParam String customerId, @RequestParam Long productId) {
        try {
            OrderNoSqlDTO orderDTO = orderNoSQLService.getOrderByCustomerAndProduct(customerId, productId);

            if (orderDTO != null) {
                return ResponseEntity.ok(orderDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/place")
    public ResponseEntity<OrderNoSqlDTO> placeOrder(
            @RequestBody PlaceOrderRequestNoSQL request) {

        logger.info("PLACING ORDER" + request.toString());
        try {
            OrderNoSqlDTO result = orderNoSQLService.placeOrder(
                    request.getCustomerId(),
                    request.getPaymentMethod(),
                    request.getShippingDetails());
            return ResponseEntity.ok(result);

        } catch (Exception e) {

            logger.info(e.toString());
            return ResponseEntity.badRequest().body(null);

        }
    }


}
