package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.nosql.services.ProductNoSQLService;
import at.wst.online_webshop.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductNoSQLController {
    private final ProductNoSQLService productNoSQLService;

    private static final Logger logger = LoggerFactory.getLogger(ProductNoSQLController.class);

    @Autowired
    public ProductNoSQLController(ProductNoSQLService productNoSQLService) {
        this.productNoSQLService = productNoSQLService;
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        logger.info("WE ARE GETTING ALL PRODUCTS FROM NOSQL");
        try{
            List<ProductDTO> products = productNoSQLService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long productId){
        logger.info("WE ARE GETTING ONE SPECIFIC PRODUCT FROM NOSQL");
        try{
            ProductDTO product = productNoSQLService.getProduct(productId);
            return ResponseEntity.ok(product);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
