package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.repositories.*;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBFiller {
    private static final int NUMBER_OF_CUSTOMERS = 1000;
    private static final int NUMBER_OF_ORDERS = 200;
    private static final int NUMBER_OF_PRODUCTS = 2000;
    private static final int NUMBER_OF_REVIEWS = 100;
    private static final int NUMBER_OF_SHOPPING_CARTS = 10;
    private static final int NUMBER_OF_VENDORS = 10;

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final VendorRepository vendorRepository;

    public DBFiller(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, ReviewRepository reviewRepository, ShoppingCartRepository shoppingCartRepository, VendorRepository vendorRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.vendorRepository = vendorRepository;
    }

    public void fillDB() {}

    private void fillCustomers() {
        var customers = new Customer[NUMBER_OF_CUSTOMERS];

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            var name = Faker.instance().name().fullName();
            var email = Faker.instance().internet().emailAddress();
            var password = Faker.instance().internet().password();
            var address = Faker.instance().address().fullAddress();

            customers[i] = new Customer(name, email, password, address);
        }

        customerRepository.saveAll(List.of(customers));
    }
}
