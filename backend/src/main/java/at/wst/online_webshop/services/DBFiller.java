package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.repositories.*;
import com.github.javafaker.Faker;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class DBFiller {
    private static final int NUMBER_OF_CUSTOMERS = 100;
    private static final int NUMBER_OF_ORDERS = 100;
    private static final int NUMBER_OF_PRODUCTS = 36;
    private static final int NUMBER_OF_REVIEWS = 300;
    private static final int NUMBER_OF_SHOPPING_CARTS = 70;
    private static final int NUMBER_OF_VENDORS = 20;
    private static final int NUMBER_OF_ORDER_ITEMS = 5;
    private static final int NUMBER_OF_CART_ITEMS = 5;

    private static final Logger logger = LoggerFactory.getLogger(DBFiller.class);


    private static final String products = "csv/products.csv";
    private final List<String> paymentMethod = List.of("Paypal", "Sofortüberweisung", "Kreditkarte");

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    @Getter
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final VendorRepository vendorRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public DBFiller(AddressRepository addressRepository, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository, CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, ReviewRepository reviewRepository, ShoppingCartRepository shoppingCartRepository, VendorRepository vendorRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.vendorRepository = vendorRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;

    }

    @Transactional
    public void clearAndFillDB() {
        //clearDB();
        fillCustomers();
        fillVendors();
        fillProducts();
        fillOrders();
        fillOrderItems();
        fillReviews();
        fillShoppingCarts();
        fillCartItems();
    }

    @Transactional
    public void clearDB() {
        System.out.println("Before clearing database...");
        logger.info("Before clearing database...");
        customerRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        reviewRepository.deleteAll();
        shoppingCartRepository.deleteAll();
        vendorRepository.deleteAll();
        vendorRepository.resetSequence();
        logger.info("After clearing database..");
        logger.info(vendorRepository.findAll().toString());
        System.out.println("After clearing database...");
    }


    public List<String[]> readFile(String filename) {
        List<String[]> records = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
             InputStreamReader isr = new InputStreamReader(is);
             CSVReader reader = new CSVReaderBuilder(isr)
                     .withCSVParser(new CSVParserBuilder()
                             .withSeparator(';')
                             .build())
                     .build()) {

            records = reader.readAll();

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return records;
    }


    public void fillCustomers() {
        var faker = Faker.instance();
        List<Customer> customers = IntStream.range(0, NUMBER_OF_CUSTOMERS)
                .parallel() // Enable parallel processing
                .mapToObj(i -> {
                    String name = faker.name().fullName();
                    String email = faker.internet().emailAddress();
                    String password = faker.internet().password();
                    Address address = new Address(
                            faker.address().streetAddress(),
                            faker.address().city(),
                            faker.address().zipCode(),
                            faker.address().country(),
                            new ArrayList<>()
                    );
                    addressRepository.save(address);
                    Customer customer = new Customer(name, email, password, address);
                    address.getCustomers().add(customer);
                    return customer;
                })
                .collect(Collectors.toList());
        customerRepository.saveAll(customers);
    }


    public void fillVendors() {
        var faker = Faker.instance();
        List<Vendor> vendors = IntStream.range(0, NUMBER_OF_VENDORS)
                .parallel()
                .mapToObj(i -> {
                    String name = faker.name().fullName();
                    String address = faker.address().fullAddress();
                    return new Vendor(name, address);
                })
                .collect(Collectors.toList());

        vendorRepository.saveAll(vendors);
        logger.info(vendorRepository.findAll().toString());
        logger.info(String.valueOf(vendorRepository.findAll().size()));
    }

    public void fillProducts() {
        var faker = Faker.instance();

        List<String[]> productFiles = readFile(this.products);

        List<Product> products = IntStream.range(0, NUMBER_OF_PRODUCTS)
                .mapToObj(i -> {
                    String[] record = productFiles.get(i % productFiles.size()); // Get the record for the current product

                    String name = record[0];
                    String category = record[1];
                    String imageFile = record[2];
                    String productDescription = faker.lorem().sentence();
                    double price = faker.number().randomDouble(2, 0, 1000);
                    String sku = faker.commerce().promotionCode();
                    int quantity = faker.number().numberBetween(0, 1000);
                    long number = faker.number().numberBetween(1, NUMBER_OF_VENDORS);
                    Vendor vendor = vendorRepository.findById(number)
                            .orElseThrow(() -> new RuntimeException("Vendor not found with the specified ID" + String.valueOf(number) + " " + vendorRepository.findAll().toString()));


                    return new Product(name, productDescription, category, price, sku, quantity, imageFile, vendor);
                })
                .collect(Collectors.toList());

        productRepository.saveAll(products);
    }

    private String getIMageFileSequentially(List<String> imageFiles, int idx) {
        int size = imageFiles.size();

        int sequentialIndex = idx % size;
        return imageFiles.get(sequentialIndex);
    }


    private void fillOrders() {
        List<Order> orders = new ArrayList<>(NUMBER_OF_ORDERS);
        var faker = Faker.instance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy")
                .withLocale(Locale.GERMAN);
        for (int i = 0; i < NUMBER_OF_ORDERS; i++) {
            Date orderDate = faker.date().between(
                    Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            int number = faker.number().numberBetween(0, 2);
            String payment = this.paymentMethod.get(number);
            String shippingDetails = faker.address().streetAddress();

            String formattedOrderDate = orderDate.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime().format(formatter);

            var customer = customerRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            orders.add(new Order(formattedOrderDate, 0, customer, new ArrayList<>(), payment, shippingDetails));
        }
        orderRepository.saveAll(orders);
    }

    private void fillOrderItems() {
        Faker faker = Faker.instance();
        List<OrderItem> orderItems = new ArrayList<>();

        for (long orderId = 1; orderId <= NUMBER_OF_ORDERS; orderId++) {
            Order order = orderRepository.findById(orderId).orElseThrow();
            BigDecimal orderTotalMount = BigDecimal.ZERO;

            for (int i = 0; i < NUMBER_OF_ORDER_ITEMS; i++) {
                Product product = productRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_PRODUCTS)).orElseThrow();
                int quantity = faker.number().numberBetween(1, NUMBER_OF_ORDER_ITEMS);

                // Check if an OrderItem with the same Product already exists in the Order
                OrderItem existingOrderItem = getOrderItemByProduct(order, product);

                if (existingOrderItem != null) {
                    // If the OrderItem already exists, increase the quantity
                    existingOrderItem.setOrderItemQuantity(existingOrderItem.getOrderItemQuantity() + quantity);

                    // Calculate and add the subtotal for the existing OrderItem
                    BigDecimal subtotal = new BigDecimal(product.getProductPrice()).multiply(BigDecimal.valueOf(existingOrderItem.getOrderItemQuantity()));
                    orderTotalMount = orderTotalMount.add(subtotal);

                    // Optionally, you can save the changes to the existing OrderItem
                    orderItemRepository.save(existingOrderItem);
                } else {
                    // If the OrderItem doesn't exist, create a new OrderItem
                    OrderItem orderItem = new OrderItem(order, product, quantity);

                    // Calculate and add the subtotal for the current OrderItem
                    BigDecimal subtotal = new BigDecimal(product.getProductPrice()).multiply(BigDecimal.valueOf(quantity));
                    orderTotalMount = orderTotalMount.add(subtotal);

                    order.getOrderItems().add(orderItem);
                    orderItems.add(orderItem);
                }
            }

            order.setOrderTotalMount(orderTotalMount.doubleValue());
        }
        orderItemRepository.saveAll(orderItems);

        updateProductTotalSells(orderItems);
    }

    private OrderItem getOrderItemByProduct(Order order, Product product) {
        return order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    private void updateProductTotalSells(List<OrderItem> orderItems) {
        Map<Product, Integer> productQuantityMap = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            Product orderedProduct = orderItem.getProduct();
            int orderQuantity = orderItem.getOrderItemQuantity();
            productQuantityMap.put(orderedProduct, productQuantityMap.getOrDefault(orderedProduct, 0) + orderQuantity);
        }

        //batch update product total sells
        for (Map.Entry<Product, Integer> entry : productQuantityMap.entrySet()) {
            Product product = entry.getKey();
            int newTotalSells = product.getProductTotalSells() + entry.getValue();
            product.setProductTotalSells(newTotalSells);
        }

        productRepository.saveAll(productQuantityMap.keySet());
    }

    private void fillReviews() {
        List<Review> reviews = new ArrayList<>(NUMBER_OF_REVIEWS);
        var faker = Faker.instance();

        for (int i = 0; i < NUMBER_OF_REVIEWS; i++) {
            var product = productRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_PRODUCTS)).orElseThrow();
            var customer = customerRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var rating = faker.number().numberBetween(1, 5);
            var comment = faker.lorem().sentence();
            var creationDate = faker.date().past(365, TimeUnit.DAYS);
            LocalDate localDate = creationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            // Format LocalDate
            String formattedDateTime = localDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            reviews.add(new Review(product, customer, rating, comment, formattedDateTime));
        }
        reviewRepository.saveAll(reviews);

        // Update the associated products with the saved reviews
        for (Review review : reviews) {
            Product product = review.getProduct();
            if (product != null) {
                List<Review> productReviews = product.getReviews();
                if (productReviews == null) {
                    productReviews = new ArrayList<>();
                    product.setReviews(productReviews);
                }
                logger.info(product.toString());
                logger.info(product.getReviews().toString());

                productReviews.add(review);
                productRepository.save(product);
            }
        }
    }

    private void fillShoppingCarts() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>(NUMBER_OF_SHOPPING_CARTS);
        var faker = Faker.instance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy")
                .withLocale(Locale.GERMAN);
        for (long customerId = 1; customerId < NUMBER_OF_SHOPPING_CARTS; customerId++) {
            Customer customer = customerRepository.findById(customerId).orElseThrow();
            Date cartDate = faker.date().between(
                    Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            String formattedCartDate = cartDate.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime().format(formatter);
            ShoppingCart shoppingCart = new ShoppingCart(customer, new ArrayList<>(), formattedCartDate);
            customer.setShoppingCart(shoppingCart);
            shoppingCarts.add(shoppingCart);
        }

        shoppingCartRepository.saveAll(shoppingCarts);
    }

    private void fillCartItems() {
        Faker faker = Faker.instance();
        List<CartItem> cartItems = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_SHOPPING_CARTS * NUMBER_OF_CART_ITEMS; i++) {
            ShoppingCart shoppingCart = shoppingCartRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_SHOPPING_CARTS)).orElseThrow();
            Product product = productRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_PRODUCTS)).orElseThrow();
            int quantity = faker.number().numberBetween(1, 5);
            double productPrice = product.getProductPrice();
            BigDecimal cartItemSubprice = BigDecimal.valueOf(productPrice).multiply(BigDecimal.valueOf(quantity));

            CartItem existingCartItem = getCartItemByProduct(shoppingCart, product);

            if (existingCartItem != null) {
                existingCartItem.setCartItemQuantity(existingCartItem.getCartItemQuantity() + quantity);
                BigDecimal subprice = existingCartItem.getCartItemSubprice().add(cartItemSubprice);
                existingCartItem.setCartItemSubprice(subprice);
            } else {
                CartItem cartItem = new CartItem(shoppingCart, product, quantity, cartItemSubprice);
                shoppingCart.getCartItems().add(cartItem);
                cartItems.add(cartItem);
            }
        }

        cartItemRepository.saveAll(cartItems);
    }

    private CartItem getCartItemByProduct(ShoppingCart shoppingCart, Product product) {
        return shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }


}
