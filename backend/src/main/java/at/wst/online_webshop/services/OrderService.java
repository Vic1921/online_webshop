package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.CustomerConvertor;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.exception_handlers.FailedOrderException;
import at.wst.online_webshop.exception_handlers.OrderNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.OrderRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static at.wst.online_webshop.convertors.OrderConvertor.convertToDto;
import static at.wst.online_webshop.convertors.OrderConvertor.convertToEntity;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;


    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return convertToDto(order);
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // TODO - Test this method
    @Transactional
    public OrderDTO placeOrder(Long customerId, Long shoppingCartId, String paymentMethod, String shippingDetails) {
        CustomerDTO customerDTO = CustomerConvertor.convertToDto(customerRepository.findById(customerId)
                .orElseThrow(() -> new FailedOrderException("Customer not found.")));
        ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertor.convertToDto(shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new FailedOrderException("Shopping cart not found.")));

        validateCart(shoppingCartDTO);
        validateOrder(paymentMethod, shippingDetails);

        var products = productRepository.findAllById(shoppingCartDTO.getProductIds());
        double totalAmount = products.stream().mapToDouble(Product::getProductPrice).sum();

        // createOrder saves the order to the database -> might create duplicate orders
        OrderDTO orderDTO = createOrder(new OrderDTO(LocalDateTime.now().toString(), totalAmount, customerId, shoppingCartDTO.getProductIds()));

        // Update product quantities
        products.forEach(product -> {
            product.setProductQuantity(product.getProductQuantity() - 1);
            productRepository.save(product);
        });

        // Save order to database
        orderRepository.save(convertToEntity(orderDTO));

        // Delete shopping cart and save
        shoppingCartRepository.deleteById(shoppingCartId);
        shoppingCartRepository.save(ShoppingCartConvertor.convertToEntity(shoppingCartDTO));
        return orderDTO;
    }

    private void validateCart(ShoppingCartDTO shoppingCartDTO) {
        productRepository.findAllById(shoppingCartDTO.getProductIds()).forEach(product -> {
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

        if (paymentMethod.length() != 16) {
            throw new FailedOrderException("Payment method must be exactly 16 characters long.");
        }
    }
}
