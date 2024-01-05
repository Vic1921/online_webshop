package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.nosql.documents.OrderDocument;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static List<OrderDTO> convertDocumentToDtoList(List<OrderDocument> cartItems) {
        return cartItems.stream()
                .map(OrderConvertorNoSQL::convertDocumentToDTO)
                .collect(Collectors.toList());
    }

    public static OrderDTO convertDocumentToDTO(OrderDocument order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        List<OrderItemDTO> orderItemDTOS = OrderItemConvertorNoSQL.convertDocumentToDtoList(order.getOrderItems());
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }


}
