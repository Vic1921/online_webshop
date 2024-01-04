package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.CustomerController;
import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.exceptions.CustomerNotFoundException;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.nosql.convertors.CustomerConvertorNoSQL;
import at.wst.online_webshop.nosql.documents.*;
import at.wst.online_webshop.nosql.repositories.*;
import at.wst.online_webshop.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class NoSQLMigrationService {
    //RDBMS
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final VendorRepository vendorRepository;

    //NoSQL
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final OrderNoSqlRepository orderNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;
    private final ReviewNoSqlRepository reviewNoSqlRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoSQLMigrationService.class);

    @Autowired
    public NoSQLMigrationService(CustomerRepository customerRepository,
                                 ProductRepository productRepository,
                                 OrderItemRepository orderItemRepository,
                                 ReviewRepository reviewRepository,
                                 ShoppingCartRepository shoppingCartRepository,
                                 CartItemRepository cartItemRepository,
                                 OrderRepository orderRepository,
                                 AddressRepository addressRepository,
                                 VendorRepository vendorRepository,
                                 CustomerNoSqlRepository customerNoSqlRepository,
                                 OrderNoSqlRepository orderNoSqlRepository,
                                 ProductNoSqlRepository productNoSqlRepository,
                                 ReviewNoSqlRepository reviewNoSqlRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.reviewRepository = reviewRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.vendorRepository = vendorRepository;
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.orderNoSqlRepository = orderNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
        this.reviewNoSqlRepository = reviewNoSqlRepository;
    }


    //we only have to migrate the customer, product, orders and reviews -> meaning iterating through the repository from rdbms
    //because those are all collections and everything else is embedded
    //shoppingcart is embedded into customer, cartitems are embedded into shoppingcart
    //orderitems are embedded into orders
    //addresses are embedded into customers
    //vendors are embedded into products

    @Transactional
    public void migrateToNoSQL(){
        clearCollections();
        migrateProducts();
        migrateCustomers();
        migrateOrders();
        //migrateReviews();
    }

    private void migrateCustomers(){
        List<Customer> rdbmsCustomers = customerRepository.findAll();

        for(Customer rdbmsCustomer : rdbmsCustomers){
            CustomerDocument nosqlCustomer = transformCustomer(rdbmsCustomer);
            customerNoSqlRepository.save(nosqlCustomer);
        }
    }

    @Transactional
    private void clearCollections(){
        productNoSqlRepository.deleteAll();
        customerNoSqlRepository.deleteAll();
        orderNoSqlRepository.deleteAll();
    }

    private void migrateProducts(){
        List<Product> rdbmsProducts = productRepository.findAll();

        for(Product rdbmsProduct : rdbmsProducts){
            ProductDocument nosqlProduct = transformProduct(rdbmsProduct);
            productNoSqlRepository.save(nosqlProduct);
        }
    }

    private void migrateOrders(){
        List<Order> rdbmsOrders = orderRepository.findAll();

        for(Order rdbmsOrder : rdbmsOrders){
            OrderDocument nosqlOrder = transformOrder(rdbmsOrder);
            orderNoSqlRepository.save(nosqlOrder);
        }

    }

    private void migrateReviews(){
        List<Review> rdbmsReviews = reviewRepository.findAll();
    }

    private OrderDocument transformOrder(Order rdbmsOrder){
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setId(String.valueOf(rdbmsOrder.getOrderId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        LocalDateTime orderDate = LocalDateTime.parse(rdbmsOrder.getOrderDate(), formatter);
        orderDocument.setOrderDate(orderDate);
        Optional<CustomerDocument> optionalCustomerDocument = customerNoSqlRepository.findById(String.valueOf(rdbmsOrder.getCustomer().getCustomerId()));
        if(optionalCustomerDocument.isPresent()){
            CustomerDocument customerdocument = optionalCustomerDocument.get();
            orderDocument.setCustomer(customerdocument);
        }else{
            logger.info("CustomerDocument not found for customerID: " + rdbmsOrder.getCustomer().getCustomerId());
            throw new CustomerNotFoundException("\"CustomerDocument in nosqlCustomerRepository when transforming cartItems not found \"");
        }
        orderDocument.setOrderTotalAmount(rdbmsOrder.getOrderTotalMount());
        List<OrderItemDocument> orderItemDocuments = transformOrderItems(rdbmsOrder.getOrderItems());
        orderDocument.setOrderItems(orderItemDocuments);

        return orderDocument;
    }

    private ProductDocument transformProduct(Product rdbmsProduct){
        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(String.valueOf(rdbmsProduct.getProductId()));
        productDocument.setProductName(rdbmsProduct.getProductName());
        productDocument.setProductPrice(rdbmsProduct.getProductPrice());
        productDocument.setProductSKU(rdbmsProduct.getProductSKU());
        productDocument.setProductCategory(rdbmsProduct.getProductCategory());
        productDocument.setProductDescription(rdbmsProduct.getProductDescription());
        productDocument.setProductQuantity(rdbmsProduct.getProductQuantity());
        productDocument.setProductImage(rdbmsProduct.getProductImageUrl());
        VendorDocument vendorDocument = transformVendor(rdbmsProduct.getVendor());
        productDocument.setVendor(vendorDocument);

        //still need to transform the reviews into nosql

        return productDocument;
    }

    private VendorDocument transformVendor(Vendor rdbmsVendor){
        VendorDocument vendorDocument = new VendorDocument();
        vendorDocument.setVendorName(rdbmsVendor.getVendorName());
        vendorDocument.setVendorAddress(rdbmsVendor.getVendorAddress());
        return vendorDocument;
    }

    private CustomerDocument transformCustomer(Customer rdbmsCustomer){
        CustomerDocument nosqlCustomer = new CustomerDocument();
        nosqlCustomer.setCustomerId(String.valueOf(rdbmsCustomer.getCustomerId()));
        nosqlCustomer.setEmail(rdbmsCustomer.getEmail());
        AddressDocument nosqlAddress = transformAddress(rdbmsCustomer.getAddress());
        if(rdbmsCustomer.getShoppingCart() != null){
            ShoppingCartDocument nosqlShoppingCart = transformShoppingCart(rdbmsCustomer.getShoppingCart());
            nosqlCustomer.setShoppingCart(nosqlShoppingCart);
        }
        nosqlCustomer.setName(rdbmsCustomer.getName());
        nosqlCustomer.setAddress(nosqlAddress);
        nosqlCustomer.setPassword(rdbmsCustomer.getPassword());
        if(rdbmsCustomer.getRecommendedBy() != null){
            Optional<CustomerDocument> optionalCustomerDocument = customerNoSqlRepository.findById(String.valueOf(rdbmsCustomer.getRecommendedBy().getCustomerId()));
            if(optionalCustomerDocument.isPresent()){
                CustomerDocument recommender = optionalCustomerDocument.get();
                nosqlCustomer.setRecommendedBy(recommender);
            }

        }
        logger.info(nosqlCustomer.toString());
        //still need to transform the reviews into nosql
        return nosqlCustomer;
    }

    private AddressDocument transformAddress(Address rdbmsAddress){
        String street = rdbmsAddress.getStreet();
        String city = rdbmsAddress.getCity();
        String postalCode = rdbmsAddress.getPostalCode();
        String country = rdbmsAddress.getCountry();
        AddressDocument addressDocument = new AddressDocument(street, city, postalCode, country);
        return addressDocument;
    }

    private ShoppingCartDocument transformShoppingCart(ShoppingCart rdbmsShoppingCart){
        ShoppingCartDocument nosqlShoppingCart = new ShoppingCartDocument();
        List<CartItemDocument> cartItemDocuments = transformCartItems(rdbmsShoppingCart.getCartItems());
        nosqlShoppingCart.setCartItems(cartItemDocuments);
        return nosqlShoppingCart;
    }

    private List<CartItemDocument> transformCartItems(List<CartItem> cartItems){
        List<CartItemDocument> cartItemDocuments = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            CartItemDocument cartItemDocument = new CartItemDocument();
            Optional<ProductDocument> optionalProductDocument = productNoSqlRepository.findById(String.valueOf(cartItem.getProduct().getProductId()));
            if (optionalProductDocument.isPresent()) {
                ProductDocument productDocument = optionalProductDocument.get();
                cartItemDocument.setProductDocument(productDocument);
            } else{
                logger.info("ProductDocument not found for productId: " + cartItem.getProduct().getProductId());
                throw new ProductNotFoundException("\"ProductDocument in nosqlProductRepository when transforming cartItems not found \"");
            }
            cartItemDocument.setCartItemQuantity(cartItem.getCartItemQuantity());
            cartItemDocument.setCartItemSubprice(cartItem.getCartItemSubprice());

            cartItemDocuments.add(cartItemDocument);
        }

        return cartItemDocuments;
    }

    private List<OrderItemDocument> transformOrderItems(List<OrderItem> orderItems){
        List<OrderItemDocument> orderItemDocuments = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            OrderItemDocument orderItemDocument = new OrderItemDocument();
            orderItemDocument.setOrderItemId(String.valueOf(orderItem.getOrderItemId()));
            orderItemDocument.setQuantity(orderItem.getOrderItemQuantity());
            Optional<ProductDocument> optionalProductDocument = productNoSqlRepository.findById(String.valueOf(orderItem.getProduct().getProductId()));

            if (optionalProductDocument.isPresent()) {
                ProductDocument productDocument = optionalProductDocument.get();
                orderItemDocument.setProduct(productDocument);
                orderItemDocuments.add(orderItemDocument);
            } else {
               logger.info("ProductDocument not found for productId: " + orderItem.getProduct().getProductId());
               throw new ProductNotFoundException("\"ProductDocument in nosqlProductRepository when transforming orderItems not found \"");

            }
        }

        return orderItemDocuments;
    }




}
