package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.exceptions.OrderNotFoundException;
import at.wst.online_webshop.nosql.convertors.OrderConvertorNoSQL;
import at.wst.online_webshop.nosql.convertors.OrderItemConvertorNoSQL;
import at.wst.online_webshop.nosql.documents.*;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.OrderNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static at.wst.online_webshop.nosql.convertors.OrderConvertorNoSQL.convertDocumentToDtoList;


@Service
public class OrderNoSQLService {

    private static final Logger logger = LoggerFactory.getLogger(OrderNoSQLService.class);
    private final OrderNoSqlRepository orderNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final MongoTemplate mongoTemplate;


    @Autowired
    public OrderNoSQLService(MongoTemplate mongoTemplate, CustomerNoSqlRepository customerNoSqlRepository, ProductNoSqlRepository productNoSqlRepository, OrderNoSqlRepository orderNoSqlRepository) {
        this.orderNoSqlRepository = orderNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<OrderDocument> orders = this.orderNoSqlRepository.findByCustomerCustomerId(String.valueOf(customerId));
        List<OrderDTO> orderDTOS = convertDocumentToDtoList(orders);
        for (OrderDTO orderDTO : orderDTOS) {
            logger.info("Order DTO details: {}", orderDTO.toString());
        }
        return orderDTOS;
    }

    public OrderDTO getOrderById(String id) {
        OrderDocument order = orderNoSqlRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        OrderDTO orderDTO = OrderConvertorNoSQL.convertDocumentToDTO(order);
        List<OrderItemDTO> orderItemDTOS = OrderItemConvertorNoSQL.convertDocumentToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }


    public OrderDTO getOrderByCustomerAndProduct(String customerId, Long productId) {
        OrderDocument order = orderNoSqlRepository.findByCustomerCustomerIdAndOrderItems_Product_ProductId(customerId, String.valueOf(productId));
        OrderDTO orderDTO = OrderConvertorNoSQL.convertDocumentToDTO(order);

        return orderDTO;
    }

    @Transactional
    public OrderDTO placeOrder(String customerId, String paymentMethod, String shippingDetails) {
        CustomerDocument customer = customerNoSqlRepository.findById(customerId).orElseThrow(() -> new FailedOrderException("Customer not found."));
        if (customer.getShoppingCart() == null) {
            throw new FailedOrderException("Shopping cart not found");
        }

        validateShoppingCart(customer.getShoppingCart());
        validateOrder(paymentMethod, shippingDetails);
        List<OrderItemDocument> orderItems = new ArrayList<>();

        for (CartItemDocument cartItem : customer.getShoppingCart().getCartItems()) {
            OrderItemDocument orderItem = new OrderItemDocument();
            orderItem.setProduct(cartItem.getProductDocument());
            orderItem.setQuantity(cartItem.getCartItemQuantity());
            orderItems.add(orderItem);
        }

        double totalMount = orderItems.stream().mapToDouble(item -> item.getProduct().getProductPrice() * item.getQuantity()).sum();
        BigDecimal roundedTotalMount = BigDecimal.valueOf(totalMount).setScale(2, RoundingMode.HALF_UP);

        List<String> productIds = customer.getShoppingCart().getCartItems().stream().map(cartItemDocument -> cartItemDocument.getProductDocument().getId()).collect(Collectors.toList());
        List<ProductDocument> productsToUpdate = new ArrayList<>();
        productNoSqlRepository.findAllById(productIds).forEach(productsToUpdate::add);

        //create Order
        OrderDocument order = new OrderDocument();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.GERMAN);
        String formattedOrderDate = LocalDateTime.now().format(formatter);
        LocalDateTime orderDate = LocalDateTime.parse(formattedOrderDate, formatter);

        order.setOrderDate(orderDate);
        order.setOrderTotalMount(roundedTotalMount.doubleValue());
        order.setCustomer(customer);
        order.setOrderItems(orderItems);

        //update product quantities
        productsToUpdate.forEach(product -> {
            product.setProductQuantity(product.getProductQuantity() - 1);
            productNoSqlRepository.save(product);
        });

        List<OrderItemDTO> orderItemDTOS = OrderItemConvertorNoSQL.convertDocumentToDtoList(order.getOrderItems());

        //save order to database
        updateProductTotalSells(orderItems);
        orderNoSqlRepository.save(order);
        BigInteger customerIdBigInt = new BigInteger(customerId, 16);
        OrderDTO newOrderDTO = new OrderDTO(order.getOrderDate().toString(), order.getOrderTotalMount(), customerIdBigInt.longValue(), orderItemDTOS);
        newOrderDTO.setOrderId(new BigInteger(order.getId(), 16).longValue());

        mongoTemplate.remove(customer.getShoppingCart());
        customer.setShoppingCart(null);
        customerNoSqlRepository.save(customer);

        return newOrderDTO;
    }

    private void updateProductTotalSells(List<OrderItemDocument> orderItems) {
        for (OrderItemDocument orderItem : orderItems) {
            ProductDocument orderedProduct = orderItem.getProduct();
            int orderQuantity = orderItem.getQuantity();

            int newTotalSells = orderedProduct.getProductTotalSells() + orderQuantity;
            orderedProduct.setProductTotalSells(newTotalSells);
            productNoSqlRepository.save(orderedProduct);
        }
    }

    private void validateShoppingCart(ShoppingCartDocument shoppingCartDocument) {
        List<String> productsIds = shoppingCartDocument.getCartItems().stream().map(cartItemDocument -> cartItemDocument.getProductDocument().getId()).collect(Collectors.toList());

        productNoSqlRepository.findAllById(productsIds).forEach(product -> {
            if (product.getProductQuantity() == 0) {
                throw new FailedOrderException("Not enough products in stock. ");
            }
        });
    }

    private void validateOrder(String paymentMethod, String shippingDetails) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new FailedOrderException("Payment method is required");
        }

        if (shippingDetails == null || shippingDetails.isEmpty()) {
            throw new FailedOrderException("Shipping details are required");
        }
    }


}
