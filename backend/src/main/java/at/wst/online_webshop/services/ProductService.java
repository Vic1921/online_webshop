package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.OrderController;
import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.OrderItem;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.Review;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.repositories.OrderItemRepository;
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
    private final OrderItemRepository orderItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    public ProductService(OrderItemRepository orderItemRepository, ProductRepository productRepository){
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
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
            ProductDTO productDTO = ProductConvertor.convertToDto(product);

            return productDTO;
        }else{
            throw new ProductNotFoundException("Product not found");
        }
    }

    public List<Product> getProductsInPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByProductPriceBetween(minPrice, maxPrice);
    }

    public int getTotalQuantitySold(Product product) {
        List<OrderItem> orderItems = orderItemRepository.findByProduct(product);
        return orderItems.stream().mapToInt(OrderItem::getOrderItemQuantity).sum();
    }
}
