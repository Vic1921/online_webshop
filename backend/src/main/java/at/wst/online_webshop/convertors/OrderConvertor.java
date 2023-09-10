package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.entities.Order;
import org.modelmapper.ModelMapper;

public class OrderConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO to Entity
    public static Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    // Entity to DTO
    public static OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
