package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.nosql.services.OrderNoSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/orders")
public class OrderNoSQLController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderNoSQLService orderNoSQLService;
    
    @GetMapping("/={customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderNoSQLService.getOrdersByCustomerId(customerId);

        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
