package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.CartItem;
import at.wst.online_webshop.exceptions.CustomerNotFoundException;
import at.wst.online_webshop.exceptions.InsufficientProductQuantityException;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.nosql.documents.CartItemDocument;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import at.wst.online_webshop.nosql.documents.ShoppingCartDocument;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static at.wst.online_webshop.nosql.convertors.CartItemsConvertorNoSql.convertDocumentToDtoList;

@Service
public class ShoppingCartNoSQLService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartNoSQLService.class);
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;


    @Autowired
    public ShoppingCartNoSQLService(ProductNoSqlRepository productNoSqlRepository, CustomerNoSqlRepository customerNoSqlRepository) {
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
    }


    //in frontend customerService updateCart() careful
    @Transactional
    public ShoppingCartDTO createShoppingCart(Long customerId) {
        CustomerDocument customerDocument = customerNoSqlRepository.findById(String.valueOf(customerId)).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        ShoppingCartDocument shoppingCartDocument = new ShoppingCartDocument(new ArrayList<>());
        customerDocument.setShoppingCart(shoppingCartDocument);
        customerNoSqlRepository.save(customerDocument);
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(customerId);

        return shoppingCartDTO;
    }

    public ShoppingCartDTO addItemToShoppingCart(Long customerId, Long productId) {
        CustomerDocument customer = customerNoSqlRepository.findById(String.valueOf(customerId))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        ProductDocument product = productNoSqlRepository.findById(String.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        int quantityToAdd = 1;

        if (product.getProductQuantity() >= quantityToAdd) {
            product.setProductQuantity(product.getProductQuantity() - quantityToAdd);
        } else {
            throw new InsufficientProductQuantityException("Not enough quantity available for the product.");
        }

        updateOrCreateCartItem(customer.getShoppingCart(), product, quantityToAdd);

        //update product and shopping cart
        productNoSqlRepository.save(product);

        return null;

    }


    public ShoppingCartDTO getShoppingCartById(Long customerId) {
        CustomerDocument customerDocument = customerNoSqlRepository.findById(String.valueOf(customerId)).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        List<CartItemDTO> cartItemDTOList = convertDocumentToDtoList(customerDocument.getShoppingCart().getCartItems());
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setCartItemDTOS(cartItemDTOList);
        shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(customerDocument.getShoppingCart()).doubleValue());
        logger.info("DTO IS BEFORE: " + shoppingCartDTO.toString());
        return shoppingCartDTO;
    }

    private BigDecimal calculateTotalPriceShoppingCart(ShoppingCartDocument shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(CartItemDocument::getCartItemSubprice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public CartItem updateOrCreateCartItem(ShoppingCartDocument shoppingCart, ProductDocument product, int quantityToAdd) {
        return null;
    }
}

