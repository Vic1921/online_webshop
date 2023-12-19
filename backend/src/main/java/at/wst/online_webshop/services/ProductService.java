package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.Review;
import at.wst.online_webshop.exception_handlers.ProductNotFoundException;
import at.wst.online_webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public ProductDTO getProduct(long productId) {
        Optional<Product> optionalProduct = this.productRepository.findById(productId);

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            List<Long> reviewIds = product.getReviews().stream().map(Review::getReviewId).collect(Collectors.toList());
            return new ProductDTO(product.getProductId(), product.getProductName(), product.getProductPrice(), product.getProductImageUrl(), product.getProductDescription(), product.getProductCategory(), product.getProductQuantity(), reviewIds);
        }else{
            throw new ProductNotFoundException("Product not found");
        }
    }
}
