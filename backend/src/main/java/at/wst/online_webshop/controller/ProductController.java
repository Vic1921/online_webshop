package at.wst.online_webshop.controller;


import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sql/products")
@CrossOrigin(origins = "https://localhost:4200")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long productId){
        try{
            ProductDTO product = productService.getProduct(productId);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
