package at.wst.online_webshop.cutomer;

import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetAllProducts() {
        // Mock data
        Product product1 = new Product("Product 1", "Description 1", "Category 1", 100.0, "SKU1", 10, "image1.jpg", null);
        Product product2 = new Product("Product 2", "Description 2", "Category 2", 200.0, "SKU2", 20, "image2.jpg", null);
        List<Product> mockProducts = Arrays.asList(product1, product2);

        // Mock behavior
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        // Call the method under test
        List<ProductDTO> result = productService.getAllProducts();

        // Verify the interaction
        Mockito.verify(productRepository, Mockito.times(1)).findAll();

        // Verify the result
        assertEquals(mockProducts, result);
    }
}
