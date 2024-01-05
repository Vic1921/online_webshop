package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.nosql.documents.CartItemDocument;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemsConvertorNoSql {

    private static ModelMapper modelMapper = new ModelMapper();

    public static List<CartItemDTO> convertDocumentToDtoList(List<CartItemDocument> cartItems) {
        return cartItems.stream()
                .map(CartItemsConvertorNoSql::convertDocumentToDTO)
                .collect(Collectors.toList());
    }

    public static CartItemDTO convertDocumentToDTO(CartItemDocument cartItem) {
        CartItemDTO cartItemDTO = modelMapper.map(cartItem, CartItemDTO.class);
        return cartItemDTO;
    }

}
