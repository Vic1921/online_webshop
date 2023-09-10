package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.exception_handlers.OrderNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.OrderRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private ModelMapper modelMapper = new ModelMapper();


    // Only for testing purposes
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

    public OrderDTO placeOrder(Long customerId, Long shoppingCartId) {
//        CustomerDTO customerDTO = customerRepository.findById(customerId).orElseThrow();

        // TODO - Implement this method
        // TODO - if fails -> throw FailedOrderException
        return new OrderDTO();
    }

    // DTO to Entity
    private Order convertToEntity(OrderDTO orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    // Entity to DTO
    private OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
