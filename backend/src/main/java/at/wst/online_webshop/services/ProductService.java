package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.convertors.OrderConvertor;
import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.Review;
import at.wst.online_webshop.exception_handlers.ProductNotFoundException;
import at.wst.online_webshop.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(){
        List<Product> products = this.productRepository.findAll();
        List<ProductDTO> productDTOS =  ProductConvertor.convertToDtoList(products);
        for (ProductDTO productDTO : productDTOS) {
            logger.info("Product DTO details: {}", productDTO.toString());
        }
        return productDTOS;
    }

    public ProductDTO getProduct(long productId) {
        Optional<Product> optionalProduct = this.productRepository.findById(productId);

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            List<Long> reviewIds = product.getReviews().stream().map(Review::getReviewId).collect(Collectors.toList());
            return new ProductDTO(product.getProductId(), product.getProductName(), product.getProductPrice(), product.getProductImageUrl(), product.getProductDescription(), product.getProductCategory(), product.getProductQuantity(), product.getVendor().getVendorName(), reviewIds);
        }else{
            throw new ProductNotFoundException("Product not found");
        }
    }
}
