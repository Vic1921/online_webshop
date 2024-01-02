package at.wst.online_webshop.services;

import at.wst.online_webshop.nosql.repositories.*;
import at.wst.online_webshop.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoSQLMigrationService {
    //RDBMS
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final ShoppingCartService shoppingCartService;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final VendorRepository vendorRepository;

    //NoSQL
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final OrderItemNoSqlRepository orderItemNoSqlRepository;
    private final OrderNoSqlRepository orderNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;
    private final ReviewNoSqlRepository reviewNoSqlRepository;
    private final ShoppingCartNoSqlRepository shoppingCartNoSqlRepository;
    private final AddressNoSqlRepository addressNoSqlRepository;
    private final VendorNoSqlRepository vendorNoSqlRepository;
    private final CartItemNoSqlRepository cartItemNoSqlRepository;

    @Autowired
    public NoSQLMigrationService(CustomerRepository customerRepository,
                                 ProductRepository productRepository,
                                 OrderItemRepository orderItemRepository,
                                 ReviewRepository reviewRepository,
                                 ShoppingCartService shoppingCartService,
                                 CartItemRepository cartItemRepository,
                                 OrderRepository orderRepository,
                                 AddressRepository addressRepository,
                                 VendorRepository vendorRepository,
                                 CustomerNoSqlRepository customerNoSqlRepository,
                                 OrderItemNoSqlRepository orderItemNoSqlRepository,
                                 OrderNoSqlRepository orderNoSqlRepository,
                                 ProductNoSqlRepository productNoSqlRepository,
                                 ReviewNoSqlRepository reviewNoSqlRepository,
                                 ShoppingCartNoSqlRepository shoppingCartNoSqlRepository,
                                 AddressNoSqlRepository addressNoSqlRepository,
                                 VendorNoSqlRepository vendorNoSqlRepository,
                                 CartItemNoSqlRepository cartItemNoSqlRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.reviewRepository = reviewRepository;
        this.shoppingCartService = shoppingCartService;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.vendorRepository = vendorRepository;
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.orderItemNoSqlRepository = orderItemNoSqlRepository;
        this.orderNoSqlRepository = orderNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
        this.reviewNoSqlRepository = reviewNoSqlRepository;
        this.shoppingCartNoSqlRepository = shoppingCartNoSqlRepository;
        this.addressNoSqlRepository = addressNoSqlRepository;
        this.vendorNoSqlRepository = vendorNoSqlRepository;
        this.cartItemNoSqlRepository = cartItemNoSqlRepository;
    }
}
