package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.convertors.*;
import at.wst.online_webshop.dtos.*;
import at.wst.online_webshop.entities.*;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.exceptions.OrderNotFoundException;
import at.wst.online_webshop.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static at.wst.online_webshop.convertors.OrderConvertor.convertToDto;
import static at.wst.online_webshop.convertors.OrderConvertor.convertToEntity;


@Service
public class OrderService {
    private OrderRepository orderRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private CustomerRepository customerRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        List<OrderItemDTO> orderItemDTOS = OrderConvertor.convertToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        OrderDTO orderDTO = OrderConvertor.convertToDto(order);
        List<OrderItemDTO> orderItemDTOS = OrderConvertor.convertToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        return convertToDto(order);
    }

    @Transactional
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO placeOrder(Long customerId, Long shoppingCartId, String paymentMethod, String shippingDetails) {
        logger.info("DO WE REACH THIS CODE");
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
        }

        double totalAmount = orderItems.stream().mapToDouble(item -> item.getProduct().getProductPrice() * item.getOrderItemQuantity()).sum();

        logger.info("DO WE REACH THIS CODE2");

        List<Long> productIds = shoppingCart.getCartItems().stream().map(CartItem::getProduct).map(Product::getProductId).collect(Collectors.toList());
        var products = productRepository.findAllById(productIds);

        // create Order
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().toString());
        order.setOrderTotalMount(totalAmount);
        order.setCustomer(customer);

        //reference Order
        final Order referenceOrder = order;
        orderItems.forEach(orderItem -> orderItem.setOrder(referenceOrder));

        order.setOrderItems(orderItems);




        // Update product quantities
        products.forEach(product -> {
            product.setProductQuantity(product.getProductQuantity() - 1);
            productRepository.save(product);
        });

        logger.info("DO WE REACH THIS CODE3");
        List<OrderItemDTO> orderItemDTOS = OrderConvertor.convertToDtoList(order.getOrderItems());


        // Save order to database
        //OrderDTO savedOrder = createOrder(convertToDto(order));
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        OrderDTO newOrderDTO = new OrderDTO(order.getOrderDate(), order.getOrderTotalMount(), order.getCustomer().getCustomerId(), orderItemDTOS);

        // Delete shopping cart and save
        shoppingCartRepository.deleteById(shoppingCartId);
        return newOrderDTO;
    }

    private void validateCart(ShoppingCart shoppingCart) {
        List<Long> productIds = shoppingCart.getCartItems().stream().map(CartItem::getCartItemId).collect(Collectors.toList());

        productRepository.findAllById(productIds).forEach(product -> {
            if (product.getProductQuantity() <= 0 ) {
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

        //ich habe das jetzt mal nur geändert weil in der website paymnet methods wir paypal, bankverbindung und kreditkarte anbieten
        if (paymentMethod.length() == 0) {
            throw new FailedOrderException("Payment shouldnt be empty");
        }
    }
}

