package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.dtos.ProductDTO;
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

    public static List<OrderItemDTO> convertToDtoList(List<OrderItem> cartItems) {
        return cartItems.stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = convertToDto(orderItem); // Convert OrderItem to OrderItemDTO

                    // Assuming there's a method convertProductToDto in ProductConvertor
                    ProductDTO productDTO = ProductConvertor.convertToDto(orderItem.getProduct()); // Convert Product to ProductDTO

                    // Set the ProductDTO in OrderItemDTO
                    orderItemDTO.setProductDTO(productDTO);

                    return orderItemDTO;
                })
                .collect(Collectors.toList());
    }

    public static OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);
        return orderItem;
    }
}
