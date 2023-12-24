package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.Review;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Long> reviewIds = product.getReviews().stream().map(Review::getReviewId).collect(Collectors.toList());
        productDTO.setReviewIds(reviewIds);
        return productDTO;
    }

    public static List<ProductDTO> convertToDtoList(List<Product> products) {
        return products.stream()
                .map(ProductConvertor::convertToDto)
                .collect(Collectors.toList());
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        return product;
    }
}
