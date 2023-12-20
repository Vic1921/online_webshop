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

    public static List<OrderItemDTO> convertToDtoList(List<OrderItem> cartItems) {
        return cartItems.stream()
                .map(OrderItemConvertor::convertToDto)
                .collect(Collectors.toList());
    }
    // Entity to DTO
    public static OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
