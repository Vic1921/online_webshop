package at.wst.online_webshop.nosql.convertors;


import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.nosql.documents.ShoppingCartDocument;
import org.modelmapper.ModelMapper;

public class ShoppingCartConvertorNoSql {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ShoppingCartDTO convertDocumentToDTO(ShoppingCartDocument shoppingCart) {
        ShoppingCartDTO shoppingCartDTO = modelMapper.map(shoppingCart, ShoppingCartDTO.class);
        return shoppingCartDTO;
    }

}
