package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.OrderItem;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO to Entity
    public static Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public static List<OrderDTO> convertToDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderConvertor::convertToDto)
                .collect(Collectors.toList());
    }
    // Entity to DTO
    public static OrderDTO convertToDto(Order order) {

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        List<OrderItemDTO> orderItemDTOS = OrderItemConvertor.convertToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }
}
