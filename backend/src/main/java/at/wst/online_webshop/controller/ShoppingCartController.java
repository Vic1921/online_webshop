package at.wst.online_webshop.controller;

import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.dtos.requests.ShoppingCartItemRequest;
import at.wst.online_webshop.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // Add an item to a shopping cart
    @PostMapping("/add-item/")
    public ResponseEntity<ShoppingCartDTO> addItemToShoppingCart(@RequestBody ShoppingCartItemRequest request) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.addItemToShoppingCart(
                request.getCustomerId(),
                request.getShoppingCartId(),
                request.getProductId());
        return ResponseEntity.ok(shoppingCartDTO);
    }


    // Create a new shopping cart
    @PostMapping("/create")
    public ResponseEntity<ShoppingCartDTO> createShoppingCart() {
        ShoppingCartDTO result = shoppingCartService.createShoppingCart(new ShoppingCartDTO());
        return ResponseEntity.ok(result);
    }

    // Get a shopping cart by its ID
    @GetMapping("/get")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartById(Long id) {
        ShoppingCartDTO shoppingCart = shoppingCartService.getShoppingCartById(id);
        return ResponseEntity.ok(shoppingCart);
    }

    // Update an existing shopping cart
    @PutMapping("/update")
    public ResponseEntity<ShoppingCartDTO> updateShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartDTO updatedShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartDTO);
        return ResponseEntity.ok(updatedShoppingCart);
    }

    // Delete a shopping cart
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteShoppingCart(Long id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }
}
