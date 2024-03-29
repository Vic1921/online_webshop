package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.convertors.OrderConvertor;
import at.wst.online_webshop.convertors.OrderItemConvertor;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.exceptions.OrderNotFoundException;
import at.wst.online_webshop.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static at.wst.online_webshop.convertors.OrderConvertor.convertToDto;
import static at.wst.online_webshop.convertors.OrderConvertor.convertToEntity;


@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private OrderRepository orderRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private CustomerRepository customerRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;
    private ShoppingCartService shoppingCartService;


    @Autowired
    public OrderService(ShoppingCartService shoppingCartService, OrderItemRepository orderItemRepository, OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        List<OrderItemDTO> orderItemDTOS = OrderItemConvertor.convertToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getOrderItems() == null) {
            logger.info("THIS IS EMPTY");
        } else {
            logger.info("THIS IS THE ORDER: {}", order.toString());
        }
        OrderDTO orderDTO = OrderConvertor.convertToDto(order);
        logger.info("THIS IS THE ORDER DTO: {}", orderDTO.toString());
        List<OrderItemDTO> orderItemDTOS = OrderItemConvertor.convertToDtoList(order.getOrderItems());
        logger.info("THIS ARE THE ORDER DTOS: {}", orderItemDTOS.toString());
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }

    @Transactional
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = this.orderRepository.findByCustomer_CustomerId(customerId);
        List<OrderDTO> orderDTOS = OrderConvertor.convertToDtoList(orders);
        for (OrderDTO orderDTO : orderDTOS) {
            logger.info("Order DTO details: {}", orderDTO.toString());
        }
        return orderDTOS;
    }

    public OrderDTO getOrderByCustomerAndProduct(Long customerId, Long productId) {
        Order order = orderRepository.findByCustomerCustomerIdAndOrderItems_Product_ProductId(customerId, productId);
        OrderDTO orderDTO = OrderConvertor.convertToDto(order);

        return orderDTO;
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO placeOrder(Long customerId, Long shoppingCartId, String paymentMethod, String shippingDetails) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new FailedOrderException("Customer not found."));

        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new FailedOrderException("Shopping cart not found."));

        validateCart(shoppingCart);
        validateOrder(paymentMethod, shippingDetails);
        List<OrderItem> orderItems = new ArrayList<>();

        //CartItem has a reference to Product
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrderItemQuantity(cartItem.getCartItemQuantity());
            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);
        }

        double totalAmount = orderItems.stream().mapToDouble(item -> item.getProduct().getProductPrice() * item.getOrderItemQuantity()).sum();
        BigDecimal roundedTotalAmount = BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP);

        List<Long> productIds = shoppingCart.getCartItems().stream().map(CartItem::getProduct).map(Product::getProductId).collect(Collectors.toList());
        var products = productRepository.findAllById(productIds);

        // create Order
        Order order = new Order();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.GERMAN);
        String formattedOrderDate = LocalDateTime.now().format(formatter);

        order.setOrderDate(formattedOrderDate);
        order.setOrderTotalMount(roundedTotalAmount.doubleValue());
        order.setCustomer(customer);
        order.setOrderPayment(paymentMethod);
        order.setOrderShippingDetails(shippingDetails);

        //reference Order
        final Order referenceOrder = order;
        orderItems.forEach(orderItem -> orderItem.setOrder(referenceOrder));
        order.setOrderItems(orderItems);

        // Update product quantities
        products.forEach(product -> {
            product.setProductQuantity(product.getProductQuantity() - 1);
            productRepository.save(product);
        });

        List<OrderItemDTO> orderItemDTOS = OrderItemConvertor.convertToDtoList(order.getOrderItems());

        // Save order to database
        updateProductTotalSells(orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        OrderDTO newOrderDTO = new OrderDTO(order.getOrderDate(), order.getOrderTotalMount(), order.getCustomer().getCustomerId(), orderItemDTOS);
        newOrderDTO.setOrderId(order.getOrderId());
        // Delete shopping cart and save
        this.shoppingCartService.deleteShoppingCart(shoppingCartId);
        return newOrderDTO;
    }

    private void updateProductTotalSells(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product orderedProduct = orderItem.getProduct();
            int orderQuantity = orderItem.getOrderItemQuantity();

            int newTotalSells = orderedProduct.getProductTotalSells() + orderQuantity;

            orderedProduct.setProductTotalSells(newTotalSells);
            productRepository.save(orderedProduct);
        }
    }

    private void validateCart(ShoppingCart shoppingCart) {
        List<Long> productIds = shoppingCart.getCartItems().stream().map(cartItem -> cartItem.getProduct().getProductId())
                .collect(Collectors.toList());

        productRepository.findAllById(productIds).forEach(product -> {
            if (product.getProductQuantity() <= 0) {
                throw new FailedOrderException("Not enough products in stock.");
            }
        });
    }

    private void validateOrder(String paymentMethod, String shippingDetails) {

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new FailedOrderException("Payment method is required.");
        }

        if (shippingDetails == null || shippingDetails.isEmpty()) {
            throw new FailedOrderException("Shipping details are required.");
        }
    }
}

