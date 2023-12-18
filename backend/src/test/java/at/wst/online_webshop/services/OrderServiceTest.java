package at.wst.online_webshop.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.OrderRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
class OrderServiceTest {
    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShoppingCartRepository shoppingCartRepository;

    /**
     * Method under test:
     * {@link OrderService#placeOrder(Long, Long, String, String)}
     */
    @Test
    void testPlaceOrder() {
        // Arrange
        Customer recommendedBy = new Customer();
        recommendedBy.setAddress("42 Main St");
        recommendedBy.setCustomerId(1L);
        recommendedBy.setEmail("jane.doe@example.org");
        recommendedBy.setName("Name");
        recommendedBy.setOrders(new ArrayList<>());
        recommendedBy.setPassword("iloveyou");
        recommendedBy.setRecommendedBy(new Customer());
        recommendedBy.setReviews(new ArrayList<>());
        recommendedBy.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartId(1L);
        shoppingCart.setCustomer(new Customer());
        shoppingCart.setProducts(new ArrayList<>());
        shoppingCart.setTotalPrice(10.0d);

        Customer recommendedBy2 = new Customer();
        recommendedBy2.setAddress("42 Main St");
        recommendedBy2.setCustomerId(1L);
        recommendedBy2.setEmail("jane.doe@example.org");
        recommendedBy2.setName("Name");
        recommendedBy2.setOrders(new ArrayList<>());
        recommendedBy2.setPassword("iloveyou");
        recommendedBy2.setRecommendedBy(recommendedBy);
        recommendedBy2.setReviews(new ArrayList<>());
        recommendedBy2.setShoppingCart(shoppingCart);

        Customer customer = new Customer();
        customer.setAddress("42 Main St");
        customer.setCustomerId(1L);
        customer.setEmail("jane.doe@example.org");
        customer.setName("Name");
        customer.setOrders(new ArrayList<>());
        customer.setPassword("iloveyou");
        customer.setRecommendedBy(new Customer());
        customer.setReviews(new ArrayList<>());
        customer.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setCartId(1L);
        shoppingCart2.setCustomer(customer);
        shoppingCart2.setProducts(new ArrayList<>());
        shoppingCart2.setTotalPrice(10.0d);

        Customer recommendedBy3 = new Customer();
        recommendedBy3.setAddress("42 Main St");
        recommendedBy3.setCustomerId(1L);
        recommendedBy3.setEmail("jane.doe@example.org");
        recommendedBy3.setName("Name");
        recommendedBy3.setOrders(new ArrayList<>());
        recommendedBy3.setPassword("iloveyou");
        recommendedBy3.setRecommendedBy(recommendedBy2);
        recommendedBy3.setReviews(new ArrayList<>());
        recommendedBy3.setShoppingCart(shoppingCart2);

        Customer recommendedBy4 = new Customer();
        recommendedBy4.setAddress("42 Main St");
        recommendedBy4.setCustomerId(1L);
        recommendedBy4.setEmail("jane.doe@example.org");
        recommendedBy4.setName("Name");
        recommendedBy4.setOrders(new ArrayList<>());
        recommendedBy4.setPassword("iloveyou");
        recommendedBy4.setRecommendedBy(new Customer());
        recommendedBy4.setReviews(new ArrayList<>());
        recommendedBy4.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart3 = new ShoppingCart();
        shoppingCart3.setCartId(1L);
        shoppingCart3.setCustomer(new Customer());
        shoppingCart3.setProducts(new ArrayList<>());
        shoppingCart3.setTotalPrice(10.0d);

        Customer customer2 = new Customer();
        customer2.setAddress("42 Main St");
        customer2.setCustomerId(1L);
        customer2.setEmail("jane.doe@example.org");
        customer2.setName("Name");
        customer2.setOrders(new ArrayList<>());
        customer2.setPassword("iloveyou");
        customer2.setRecommendedBy(recommendedBy4);
        customer2.setReviews(new ArrayList<>());
        customer2.setShoppingCart(shoppingCart3);

        ShoppingCart shoppingCart4 = new ShoppingCart();
        shoppingCart4.setCartId(1L);
        shoppingCart4.setCustomer(customer2);
        shoppingCart4.setProducts(new ArrayList<>());
        shoppingCart4.setTotalPrice(10.0d);

        Customer customer3 = new Customer();
        customer3.setAddress("42 Main St");
        customer3.setCustomerId(1L);
        customer3.setEmail("jane.doe@example.org");
        customer3.setName("Name");
        customer3.setOrders(new ArrayList<>());
        customer3.setPassword("iloveyou");
        customer3.setRecommendedBy(recommendedBy3);
        customer3.setReviews(new ArrayList<>());
        customer3.setShoppingCart(shoppingCart4);
        Optional<Customer> ofResult = Optional.of(customer3);
        when(customerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(productRepository.findAllById(Mockito.<Iterable<Long>>any())).thenReturn(new ArrayList<>());

        Customer recommendedBy5 = new Customer();
        recommendedBy5.setAddress("42 Main St");
        recommendedBy5.setCustomerId(1L);
        recommendedBy5.setEmail("jane.doe@example.org");
        recommendedBy5.setName("Name");
        recommendedBy5.setOrders(new ArrayList<>());
        recommendedBy5.setPassword("iloveyou");
        recommendedBy5.setRecommendedBy(new Customer());
        recommendedBy5.setReviews(new ArrayList<>());
        recommendedBy5.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart5 = new ShoppingCart();
        shoppingCart5.setCartId(1L);
        shoppingCart5.setCustomer(new Customer());
        shoppingCart5.setProducts(new ArrayList<>());
        shoppingCart5.setTotalPrice(10.0d);

        Customer recommendedBy6 = new Customer();
        recommendedBy6.setAddress("42 Main St");
        recommendedBy6.setCustomerId(1L);
        recommendedBy6.setEmail("jane.doe@example.org");
        recommendedBy6.setName("Name");
        recommendedBy6.setOrders(new ArrayList<>());
        recommendedBy6.setPassword("iloveyou");
        recommendedBy6.setRecommendedBy(recommendedBy5);
        recommendedBy6.setReviews(new ArrayList<>());
        recommendedBy6.setShoppingCart(shoppingCart5);

        Customer customer4 = new Customer();
        customer4.setAddress("42 Main St");
        customer4.setCustomerId(1L);
        customer4.setEmail("jane.doe@example.org");
        customer4.setName("Name");
        customer4.setOrders(new ArrayList<>());
        customer4.setPassword("iloveyou");
        customer4.setRecommendedBy(new Customer());
        customer4.setReviews(new ArrayList<>());
        customer4.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart6 = new ShoppingCart();
        shoppingCart6.setCartId(1L);
        shoppingCart6.setCustomer(customer4);
        shoppingCart6.setProducts(new ArrayList<>());
        shoppingCart6.setTotalPrice(10.0d);

        Customer customer5 = new Customer();
        customer5.setAddress("42 Main St");
        customer5.setCustomerId(1L);
        customer5.setEmail("jane.doe@example.org");
        customer5.setName("Name");
        customer5.setOrders(new ArrayList<>());
        customer5.setPassword("iloveyou");
        customer5.setRecommendedBy(recommendedBy6);
        customer5.setReviews(new ArrayList<>());
        customer5.setShoppingCart(shoppingCart6);

        ShoppingCart shoppingCart7 = new ShoppingCart();
        shoppingCart7.setCartId(1L);
        shoppingCart7.setCustomer(customer5);
        shoppingCart7.setProducts(new ArrayList<>());
        shoppingCart7.setTotalPrice(10.0d);
        Optional<ShoppingCart> ofResult2 = Optional.of(shoppingCart7);
        when(shoppingCartRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(FailedOrderException.class,
                () -> orderService.placeOrder(1L, 1L, "Payment Method", "Shipping Details"));
        verify(productRepository).findAllById(Mockito.<Iterable<Long>>any());
        verify(customerRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link OrderService#placeOrder(Long, Long, String, String)}
     */
    @Test
    void testPlaceOrder2() {
        // Arrange
        Customer recommendedBy = new Customer();
        recommendedBy.setAddress("42 Main St");
        recommendedBy.setCustomerId(1L);
        recommendedBy.setEmail("jane.doe@example.org");
        recommendedBy.setName("Name");
        recommendedBy.setOrders(new ArrayList<>());
        recommendedBy.setPassword("iloveyou");
        recommendedBy.setRecommendedBy(new Customer());
        recommendedBy.setReviews(new ArrayList<>());
        recommendedBy.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartId(1L);
        shoppingCart.setCustomer(new Customer());
        shoppingCart.setProducts(new ArrayList<>());
        shoppingCart.setTotalPrice(10.0d);

        Customer recommendedBy2 = new Customer();
        recommendedBy2.setAddress("42 Main St");
        recommendedBy2.setCustomerId(1L);
        recommendedBy2.setEmail("jane.doe@example.org");
        recommendedBy2.setName("Name");
        recommendedBy2.setOrders(new ArrayList<>());
        recommendedBy2.setPassword("iloveyou");
        recommendedBy2.setRecommendedBy(recommendedBy);
        recommendedBy2.setReviews(new ArrayList<>());
        recommendedBy2.setShoppingCart(shoppingCart);

        Customer customer = new Customer();
        customer.setAddress("42 Main St");
        customer.setCustomerId(1L);
        customer.setEmail("jane.doe@example.org");
        customer.setName("Name");
        customer.setOrders(new ArrayList<>());
        customer.setPassword("iloveyou");
        customer.setRecommendedBy(new Customer());
        customer.setReviews(new ArrayList<>());
        customer.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setCartId(1L);
        shoppingCart2.setCustomer(customer);
        shoppingCart2.setProducts(new ArrayList<>());
        shoppingCart2.setTotalPrice(10.0d);

        Customer recommendedBy3 = new Customer();
        recommendedBy3.setAddress("42 Main St");
        recommendedBy3.setCustomerId(1L);
        recommendedBy3.setEmail("jane.doe@example.org");
        recommendedBy3.setName("Name");
        recommendedBy3.setOrders(new ArrayList<>());
        recommendedBy3.setPassword("iloveyou");
        recommendedBy3.setRecommendedBy(recommendedBy2);
        recommendedBy3.setReviews(new ArrayList<>());
        recommendedBy3.setShoppingCart(shoppingCart2);

        Customer recommendedBy4 = new Customer();
        recommendedBy4.setAddress("42 Main St");
        recommendedBy4.setCustomerId(1L);
        recommendedBy4.setEmail("jane.doe@example.org");
        recommendedBy4.setName("Name");
        recommendedBy4.setOrders(new ArrayList<>());
        recommendedBy4.setPassword("iloveyou");
        recommendedBy4.setRecommendedBy(new Customer());
        recommendedBy4.setReviews(new ArrayList<>());
        recommendedBy4.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart3 = new ShoppingCart();
        shoppingCart3.setCartId(1L);
        shoppingCart3.setCustomer(new Customer());
        shoppingCart3.setProducts(new ArrayList<>());
        shoppingCart3.setTotalPrice(10.0d);

        Customer customer2 = new Customer();
        customer2.setAddress("42 Main St");
        customer2.setCustomerId(1L);
        customer2.setEmail("jane.doe@example.org");
        customer2.setName("Name");
        customer2.setOrders(new ArrayList<>());
        customer2.setPassword("iloveyou");
        customer2.setRecommendedBy(recommendedBy4);
        customer2.setReviews(new ArrayList<>());
        customer2.setShoppingCart(shoppingCart3);

        ShoppingCart shoppingCart4 = new ShoppingCart();
        shoppingCart4.setCartId(1L);
        shoppingCart4.setCustomer(customer2);
        shoppingCart4.setProducts(new ArrayList<>());
        shoppingCart4.setTotalPrice(10.0d);

        Customer customer3 = new Customer();
        customer3.setAddress("42 Main St");
        customer3.setCustomerId(1L);
        customer3.setEmail("jane.doe@example.org");
        customer3.setName("Name");
        customer3.setOrders(new ArrayList<>());
        customer3.setPassword("iloveyou");
        customer3.setRecommendedBy(recommendedBy3);
        customer3.setReviews(new ArrayList<>());
        customer3.setShoppingCart(shoppingCart4);
        Optional<Customer> ofResult = Optional.of(customer3);
        when(customerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(productRepository.findAllById(Mockito.<Iterable<Long>>any()))
                .thenThrow(new FailedOrderException("An error occurred"));

        Customer recommendedBy5 = new Customer();
        recommendedBy5.setAddress("42 Main St");
        recommendedBy5.setCustomerId(1L);
        recommendedBy5.setEmail("jane.doe@example.org");
        recommendedBy5.setName("Name");
        recommendedBy5.setOrders(new ArrayList<>());
        recommendedBy5.setPassword("iloveyou");
        recommendedBy5.setRecommendedBy(new Customer());
        recommendedBy5.setReviews(new ArrayList<>());
        recommendedBy5.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart5 = new ShoppingCart();
        shoppingCart5.setCartId(1L);
        shoppingCart5.setCustomer(new Customer());
        shoppingCart5.setProducts(new ArrayList<>());
        shoppingCart5.setTotalPrice(10.0d);

        Customer recommendedBy6 = new Customer();
        recommendedBy6.setAddress("42 Main St");
        recommendedBy6.setCustomerId(1L);
        recommendedBy6.setEmail("jane.doe@example.org");
        recommendedBy6.setName("Name");
        recommendedBy6.setOrders(new ArrayList<>());
        recommendedBy6.setPassword("iloveyou");
        recommendedBy6.setRecommendedBy(recommendedBy5);
        recommendedBy6.setReviews(new ArrayList<>());
        recommendedBy6.setShoppingCart(shoppingCart5);

        Customer customer4 = new Customer();
        customer4.setAddress("42 Main St");
        customer4.setCustomerId(1L);
        customer4.setEmail("jane.doe@example.org");
        customer4.setName("Name");
        customer4.setOrders(new ArrayList<>());
        customer4.setPassword("iloveyou");
        customer4.setRecommendedBy(new Customer());
        customer4.setReviews(new ArrayList<>());
        customer4.setShoppingCart(new ShoppingCart());

        ShoppingCart shoppingCart6 = new ShoppingCart();
        shoppingCart6.setCartId(1L);
        shoppingCart6.setCustomer(customer4);
        shoppingCart6.setProducts(new ArrayList<>());
        shoppingCart6.setTotalPrice(10.0d);

        Customer customer5 = new Customer();
        customer5.setAddress("42 Main St");
        customer5.setCustomerId(1L);
        customer5.setEmail("jane.doe@example.org");
        customer5.setName("Name");
        customer5.setOrders(new ArrayList<>());
        customer5.setPassword("iloveyou");
        customer5.setRecommendedBy(recommendedBy6);
        customer5.setReviews(new ArrayList<>());
        customer5.setShoppingCart(shoppingCart6);

        ShoppingCart shoppingCart7 = new ShoppingCart();
        shoppingCart7.setCartId(1L);
        shoppingCart7.setCustomer(customer5);
        shoppingCart7.setProducts(new ArrayList<>());
        shoppingCart7.setTotalPrice(10.0d);
        Optional<ShoppingCart> ofResult2 = Optional.of(shoppingCart7);
        when(shoppingCartRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(FailedOrderException.class,
                () -> orderService.placeOrder(1L, 1L, "Payment Method", "Shipping Details"));
        verify(productRepository).findAllById(Mockito.<Iterable<Long>>any());
        verify(customerRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findById(Mockito.<Long>any());
    }
}
