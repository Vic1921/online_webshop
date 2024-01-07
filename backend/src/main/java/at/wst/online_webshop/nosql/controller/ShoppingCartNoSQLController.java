package at.wst.online_webshop.nosql.controller;


import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.nosql.request.ShoppingCartItemRequestNoSQL;
import at.wst.online_webshop.nosql.services.ShoppingCartNoSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/shopping-cart")
public class ShoppingCartNoSQLController {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartNoSQLController.class);
    @Autowired
    private ShoppingCartNoSQLService shoppingCartNoSQLService;

    @PostMapping("/create")
    public ResponseEntity<ShoppingCartDTO> createShoppingCart(@RequestBody String customerId) {
        ShoppingCartDTO result = shoppingCartNoSQLService.createShoppingCart(customerId);
        logger.info("Creating: " + result.toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-items")
    public ResponseEntity<List<CartItemDTO>> getItemsFromShoppingCart(@RequestParam Long id) {
        logger.info("Shopping Cart items are being fetched from nosql ");
        ShoppingCartDTO shoppingCartDTO = shoppingCartNoSQLService.getShoppingCartById(id);
        List<CartItemDTO> productsFromShoppingCart = shoppingCartDTO.getCartItemDTOS();
        return ResponseEntity.ok(productsFromShoppingCart);
    }

    @GetMapping("/get")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartById(@RequestParam Long id) {
        ShoppingCartDTO shoppingCart = shoppingCartNoSQLService.getShoppingCartById(id);
        logger.info("Get " + shoppingCart.toString());
        return ResponseEntity.ok(shoppingCart);
    }

    @PostMapping("/add-item/")
    public ResponseEntity<ShoppingCartDTO> addItemToShoppingCart(@RequestBody ShoppingCartItemRequestNoSQL request) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartNoSQLService.addItemToShoppingCart(
                request.getCustomerId(),
                request.getProductId());
        logger.info("ADDING: " + shoppingCartDTO.toString());
        return ResponseEntity.ok(shoppingCartDTO);
    }
}

