package at.wst.online_webshop.controller;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.requests.PlaceOrderRequest;
import at.wst.online_webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place an order
    @PostMapping("/place")
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestBody PlaceOrderRequest request) {

        OrderDTO result = orderService.placeOrder(
                request.getCustomerId(),
                request.getShoppingCartId());

        return ResponseEntity.ok(result);
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
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO, @PathVariable Long id) {
        orderDTO.setOrderId(id);  // Ensure the ID in the DTO matches the path variable
        OrderDTO updatedOrder = orderService.updateOrder(orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }
}