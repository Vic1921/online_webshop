import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import at.wst.online_webshop.OnlineWebshopApplication;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.repositories.CartItemRepository;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import at.wst.online_webshop.services.CustomerService;
import at.wst.online_webshop.services.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = OnlineWebshopApplication.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    @Transactional
    void testAddItemToShoppingCart() {
        // Mock data
        Long customerId = 1L;
        Long shoppingCartId = 2L;
        Long productId = 3L;

        Address address = new Address("Währingerstraße 29", "Vienna", "1010", "Austria", new ArrayList<>());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.GERMAN);
        String formattedCartDate = LocalDateTime.now().format(formatter);
        ShoppingCart existingShoppingCart = new ShoppingCart(null, new ArrayList<CartItem>(), formattedCartDate);
        Customer existingCustomer = new Customer("John Doe", "john@example.com", "password", address);
        Product existingProduct = new Product("Product", "Description", "Category", 10.0, "SKU", 20, "image.jpg", null);
        address.getCustomers().add(existingCustomer);

        when(shoppingCartRepository.findById(shoppingCartId)).thenReturn(Optional.of(existingShoppingCart));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        int initialProductQuantity = existingProduct.getProductQuantity();
        int quantityToAdd = 1;


        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);

        // Call the service method
        ShoppingCartDTO result = shoppingCartService.addItemToShoppingCart(customerId, shoppingCartId, productId);

        // Assertions
        assertNotNull(result);

        Double sum = existingShoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getCartItemSubprice().doubleValue())
                .reduce(0.0, Double::sum);

        assertEquals(sum, result.getTotalPrice(), 0.001);
        // Add more assertions based on your DTO structure

        // Verify that save methods were called
        verify(productRepository, times(1)).save(existingProduct);
        verify(shoppingCartRepository, times(1)).save(existingShoppingCart);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));  // You may need to adjust this based on your CartItem structure
    }
}
