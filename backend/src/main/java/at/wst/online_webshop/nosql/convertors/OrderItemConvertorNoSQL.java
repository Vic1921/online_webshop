package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.OrderItemDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.nosql.documents.OrderItemDocument;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static List<OrderItemDTO> convertDocumentToDtoList(List<OrderItemDocument> orderItems) {
        return orderItems.stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = convertDocumentToDTO(orderItem); // Convert OrderItem to OrderItemDTO

                    // Assuming there's a method convertProductToDto in ProductConvertor
                    ProductDTO productDTO = ProductConvertorNoSQL.convertDocumentToDTO(orderItem.getProduct()); // Convert Product to ProductDTO

                    // Set the ProductDTO in OrderItemDTO
                    orderItemDTO.setProductDTO(productDTO);

                    return orderItemDTO;
                })
                .collect(Collectors.toList());
    }

    public static OrderItemDTO convertDocumentToDTO(OrderItemDocument orderItemDocument) {
        OrderItemDTO orderItemDTO = modelMapper.map(orderItemDocument, OrderItemDTO.class);
        return orderItemDTO;
    }

}
