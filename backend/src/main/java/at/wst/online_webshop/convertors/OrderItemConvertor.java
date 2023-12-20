package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.entities.CartItem;
import at.wst.online_webshop.entities.OrderItem;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemConvertor {
    private static ModelMapper modelMapper = new ModelMapper();

    public static OrderItemDTO convertToDto(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
        return orderItemDTO;
    }

    public static OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);
        return orderItem;
    }
}
