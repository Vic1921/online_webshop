package at.wst.online_webshop.controller;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.dtos.requests.ShoppingCartItemRequest;
import at.wst.online_webshop.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Get all items from the shopping cart
    @GetMapping("/get-items")
    public ResponseEntity<List<CartItemDTO>> getItemsFromShoppingCart(@RequestParam Long id) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCartById(id);
        List<CartItemDTO> productsFromShoppingCart = shoppingCartDTO.getCartItemDTOS();
        return ResponseEntity.ok(productsFromShoppingCart);
    }

    // Create a new shopping cart
    @PostMapping("/create")
    public ResponseEntity<ShoppingCartDTO> createShoppingCart() {
        ShoppingCartDTO result = shoppingCartService.createShoppingCart(new ShoppingCartDTO());
        return ResponseEntity.ok(result);
    }

    // Get a shopping cart by its ID
    @GetMapping("/get")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartById(@RequestParam Long id) {
        ShoppingCartDTO shoppingCart = shoppingCartService.getShoppingCartById(id);
        return ResponseEntity.ok(shoppingCart);
    }

    // Update an existing shopping cart
    @PutMapping("/update")
    public ResponseEntity<ShoppingCartDTO> updateShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartDTO updatedShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartDTO);
        return ResponseEntity.ok(updatedShoppingCart);
    }

    // Delete a shopping cart
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteShoppingCart(@RequestParam Long id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }
}
