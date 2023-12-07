import at.wst.online_webshop.OnlineWebshopApplication;
import at.wst.online_webshop.controller.ProductController;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.Vendor;
import at.wst.online_webshop.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OnlineWebshopApplication.class)
public class ProductControllerTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    public void testGetAllProducts() {
        // Arrange
        Vendor vendor1 = new Vendor("Vendor 1", "Währingerstraße 29");

        List<Product> mockProducts = Arrays.asList(
            new Product("Product 1", "lorem ipsum", "Ski", 99.99, "432423", 30, "dsffs.jpg", vendor1 ),
            new Product("Product 2", "lorem ipsum", "Ausrüstung", 108.99, "432423", 39, "dsffs.jpg", vendor1 )
        );


            // Mock the productService to return the mockProducts when getAllProducts is called
        when(productService.getAllProducts()).thenReturn(mockProducts);

        // Act
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockProducts, responseEntity.getBody());
    }
}
