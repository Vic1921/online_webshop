package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.ShoppingCart;
import org.modelmapper.ModelMapper;

public class ShoppingCartConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ShoppingCartDTO convertToDto(ShoppingCart shoppingCart) {
        ShoppingCartDTO shoppingCartDTO = modelMapper.map(shoppingCart, ShoppingCartDTO.class);
        return shoppingCartDTO;
    }

    public static ShoppingCart convertToEntity(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = modelMapper.map(shoppingCartDTO, ShoppingCart.class);
        if (shoppingCart.getCartItems() != null) {
            shoppingCart.getCartItems().forEach(cartItem -> cartItem.setShoppingCart(shoppingCart));
        }
        return shoppingCart;
    }
}
