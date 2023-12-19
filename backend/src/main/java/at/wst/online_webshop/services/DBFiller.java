package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.repositories.*;
import com.github.javafaker.Faker;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class DBFiller {
    private static final int NUMBER_OF_CUSTOMERS = 100;
    private static final int NUMBER_OF_ORDERS = 100;
    private static final int NUMBER_OF_PRODUCTS = 36;
    private static final int NUMBER_OF_REVIEWS = 10;
    private static final int NUMBER_OF_SHOPPING_CARTS = 10;
    private static final int NUMBER_OF_VENDORS = 20;

    private static final Logger logger = LoggerFactory.getLogger(DBFiller.class);

    private static final String imageFiles = "src/main/csv/filenames.csv";
    private static final String productNames = "src/main/csv/articlenames.csv";
    private static final String productCategories = "src/main/csv/artikelkategorie.csv";
    private static final String products = "src/main/csv/products.csv";

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final VendorRepository vendorRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public DBFiller(CartItemRepository cartItemRepository, CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, ReviewRepository reviewRepository, ShoppingCartRepository shoppingCartRepository, VendorRepository vendorRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.vendorRepository = vendorRepository;
        this.cartItemRepository = cartItemRepository;

    }

    @Transactional
    public void clearAndFillDB() {
        //clearDB();
        fillCustomers();
        fillVendors();
        fillProducts();
        //fillOrders();
        //fillReviews();
        //fillShoppingCarts();
    }

    @Transactional
    public void clearDB() {
        System.out.println("Before clearing database...");
        logger.info("Before clearing database...");
        customerRepository.deleteAll();
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

    public ProductRepository getProductRepository(){
        return this.productRepository;
    }

    public List<String[]>  readFile(String filename) {
        List<String[]> records = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filename))
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
                    String address = faker.address().fullAddress();
                    return new Customer(name, email, password, address);
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
        logger.info("FILL PRODUCT " + this.vendorRepository.findAll().toString());

        List<Product> products = IntStream.range(0, NUMBER_OF_PRODUCTS)
                //i dont know why you put parallel it fucked everything up i can't get the right amoutn of entries from vendorRepository the number always changed
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

    private String getIMageFileSequentially(List<String> imageFiles, int idx){
        int size = imageFiles.size();

        int sequentialIndex = idx % size;
        return imageFiles.get(sequentialIndex);
    }


    private void fillOrders() {
        List<Order> orders = new ArrayList<>(NUMBER_OF_ORDERS);
        var faker = Faker.instance();

        for (int i = 0; i < NUMBER_OF_ORDERS; i++) {
            var orderDate = faker.date().birthday();
            var customer = customerRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var products = productRepository.findAll()
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                                Collections.shuffle(list);
                                return list.stream().limit(5).collect(Collectors.toList());
                            }));
            var totalPrice = products.stream().mapToDouble(Product::getProductPrice).sum();

            orders.add(new Order(orderDate, totalPrice, customer, products));
        }
        orderRepository.saveAll(orders);
    }

    private void fillReviews() {
        List<Review> reviews = new ArrayList<>(NUMBER_OF_REVIEWS);
        var faker = Faker.instance();

        for (int i = 0; i < NUMBER_OF_REVIEWS; i++) {
            var product = productRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_PRODUCTS)).orElseThrow();
            var customer = customerRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var rating = faker.number().numberBetween(1, 5);
            var comment = faker.lorem().sentence();

            reviews.add(new Review(product, customer, rating, comment));
        }
        reviewRepository.saveAll(reviews);
    }

    private void fillShoppingCarts() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>(NUMBER_OF_SHOPPING_CARTS);
        var faker = Faker.instance();

        for (int i = 0; i < NUMBER_OF_SHOPPING_CARTS; i++) {
            var customer = customerRepository.findById((long) faker.number().numberBetween(1, NUMBER_OF_CUSTOMERS)).orElseThrow();
            var cartItems = cartItemRepository.findAll()
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        Collections.shuffle(list);
                        return list.stream().limit(5).collect(Collectors.toList());
                    }));
            shoppingCarts.add(new ShoppingCart(customer, cartItems));
        }
        shoppingCartRepository.saveAll(shoppingCarts);
    }
}
