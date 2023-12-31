package at.wst.online_webshop.controller;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.requests.PlaceOrderRequest;
import at.wst.online_webshop.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/sql/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    // Place an order
    @PostMapping("/place")
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestBody PlaceOrderRequest request) {

        logger.info("PLACING ORDER" + request.toString());
        try {
            OrderDTO result = orderService.placeOrder(
                    request.getCustomerId(),
                    request.getShoppingCartId(),
                    request.getPaymentMethod(),
                    request.getShippingDetails());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.badRequest().body(null);

        }
    }

    // Create a new order (Directly using OrderDTO)
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO result = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(result);
    }

    // Get an order by its ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrderById(id);
            logger.info("THIS IS THE ORDER");
            logger.info(order.toString());
            return ResponseEntity.ok(order);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/order-details")
    public ResponseEntity<OrderDTO> getOrderByProductAndCustomer(@RequestParam Long customerId, @RequestParam Long productId){
        try{
            OrderDTO orderDTO = orderService.getOrderByCustomerAndProduct(customerId, productId);

            if (orderDTO != null) {
                return ResponseEntity.ok(orderDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO, @PathVariable Long id) {
        orderDTO.setOrderId(id);  // Ensure the ID in the DTO matches the path variable
        OrderDTO updatedOrder = orderService.updateOrder(orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/={customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);

        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }
}