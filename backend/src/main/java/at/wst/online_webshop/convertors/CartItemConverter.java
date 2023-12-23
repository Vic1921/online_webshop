package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.entities.CartItem;
import at.wst.online_webshop.entities.Customer;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemConverter {
    private static ModelMapper modelMapper = new ModelMapper();

    public static CartItemDTO convertToDto(CartItem cartItem) {
        CartItemDTO cartItemDTO = modelMapper.map(cartItem, CartItemDTO.class);
        cartItemDTO.setShoppingCartDTO(ShoppingCartConvertor.convertToDto(cartItem.getShoppingCart()));

        return cartItemDTO;
    }

    public static List<CartItemDTO> convertToDtoList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public static CartItem convertToEntity(CartItemDTO cartItemDTO) {
        CartItem cartItem = modelMapper.map(cartItemDTO, CartItem.class);
        cartItem.setShoppingCart(ShoppingCartConvertor.convertToEntity(cartItemDTO.getShoppingCartDTO()));
        return cartItem;
    }
}
