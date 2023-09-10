package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Product;
import org.modelmapper.ModelMapper;

public class ProductConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        return product;
    }
}
