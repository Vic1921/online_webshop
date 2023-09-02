package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.repositories.*;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public void fillDB() {
        fillCustomers();
        fillVendors();
        fillProducts();
        fillOrders();
        fillReviews();
        fillShoppingCarts();
    }

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
    private void fillVendors() {
        var vendors = new Vendor[NUMBER_OF_VENDORS];

        for (int i = 0; i < NUMBER_OF_VENDORS; i++) {
            var name = Faker.instance().name().fullName();
            var address = Faker.instance().address().fullAddress();

            vendors[i] = new Vendor(name, address);
        }
        vendorRepository.saveAll(List.of(vendors));
    }
    private void fillProducts() {
        var products = new Product[NUMBER_OF_PRODUCTS];

        for (int i = 0; i < NUMBER_OF_PRODUCTS; i++) {
            var name = Faker.instance().commerce().productName();
            var price = Faker.instance().number().randomDouble(2, 0, 1000);
            var sku = Faker.instance().commerce().promotionCode();
            var quantity = Faker.instance().number().numberBetween(0, 1000);
            var vendor = vendorRepository.findById((long) Faker.instance().number().numberBetween(1, NUMBER_OF_VENDORS)).orElseThrow();

            products[i] = new Product(name, price, sku, quantity, vendor);
        }
        productRepository.saveAll(List.of(products));
    }
    private void fillOrders() {
        var orders = new Order[NUMBER_OF_ORDERS];

        for (int i = 0; i < NUMBER_OF_ORDERS; i++) {
            var orderDate = Faker.instance().date().birthday();
            var customer = customerRepository.findById((long) Faker.instance().number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var products = productRepository.findAll()
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                                Collections.shuffle(list);
                                return list.stream().limit(5).collect(Collectors.toList());
                            }));
            var totalPrice = products.stream().mapToDouble(Product::getProductPrice).sum();

            orders[i] = new Order(orderDate, totalPrice, customer, products);
        }
        orderRepository.saveAll(List.of(orders));
    }
    private void fillReviews() {
        var reviews = new Review[NUMBER_OF_REVIEWS];

        for (int i = 0; i < NUMBER_OF_REVIEWS; i++) {
            var product = productRepository.findById((long) Faker.instance().number().numberBetween(1, NUMBER_OF_PRODUCTS)).orElseThrow();
            var customer = customerRepository.findById((long) Faker.instance().number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var rating = Faker.instance().number().numberBetween(1, 5);
            var comment = Faker.instance().lorem().sentence();

            reviews[i] = new Review(product, customer, rating, comment);
        }
        reviewRepository.saveAll(List.of(reviews));
    }
    private void fillShoppingCarts() {
        var shoppingCarts = new ShoppingCart[NUMBER_OF_SHOPPING_CARTS];

        for (int i = 0; i < NUMBER_OF_SHOPPING_CARTS; i++) {
            var totalPrice = Faker.instance().number().randomDouble(2, 0, 1000);
            var customer = customerRepository.findById((long) Faker.instance().number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var products = productRepository.findAll()
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        Collections.shuffle(list);
                        return list.stream().limit(5).collect(Collectors.toList());
                    }));
            shoppingCarts[i] = new ShoppingCart(totalPrice, customer, products);
        }
        shoppingCartRepository.saveAll(List.of(shoppingCarts));
    }
}
