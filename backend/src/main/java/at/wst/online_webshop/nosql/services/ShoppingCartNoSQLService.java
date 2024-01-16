package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.convertors.CartItemConverter;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.CartItemDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.CartItem;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exceptions.CustomerNotFoundException;
import at.wst.online_webshop.exceptions.InsufficientProductQuantityException;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.exceptions.ShoppingCartNotFoundException;
import at.wst.online_webshop.nosql.convertors.CartItemsConvertorNoSql;
import at.wst.online_webshop.nosql.convertors.ShoppingCartConvertorNoSql;
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
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    public ShoppingCartDTO createShoppingCart(String customerId) {
        CustomerDocument customerDocument = customerNoSqlRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.GERMAN);
        String formattedCartDate = LocalDateTime.now().format(formatter);
        LocalDateTime cartDate = LocalDateTime.parse(formattedCartDate, formatter);
        ShoppingCartDocument shoppingCartDocument = new ShoppingCartDocument(new ArrayList<>(), cartDate);
        customerDocument.setShoppingCart(shoppingCartDocument);
        customerNoSqlRepository.save(customerDocument);

        BigInteger customerIdBigInt = new BigInteger(customerId, 16);

        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(customerIdBigInt.longValue());
        logger.info("CREATING CART");
        logger.info(shoppingCartDTO.toString());
        return shoppingCartDTO;
    }

    @Transactional
    public ShoppingCartDTO addItemToShoppingCart(String customerId, Long productId) {
        CustomerDocument customer = customerNoSqlRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        ProductDocument product = productNoSqlRepository.findById(String.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        int quantityToAdd = 1;

        if (product.getProductQuantity() < quantityToAdd) {
            throw new InsufficientProductQuantityException("Not enough quantity available for the product.");
        }

        updateOrCreateCartItem(customer.getShoppingCart(), product, quantityToAdd);

        //update product and shopping cart
        productNoSqlRepository.save(product);
        customerNoSqlRepository.save(customer);
        logger.info("SHOPPING CART IS ");
        logger.info(customer.getShoppingCart().toString());


        List<CartItemDTO> cartItemDTOS = CartItemsConvertorNoSql.convertDocumentToDtoList(customer.getShoppingCart().getCartItems());
        ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertorNoSql.convertDocumentToDTO(customer.getShoppingCart());
        BigInteger customerIdBigInt = new BigInteger(customerId, 16);
        shoppingCartDTO.setCustomerId(customerIdBigInt.longValue());
        shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(customer.getShoppingCart()).doubleValue());
        shoppingCartDTO.setCartItemDTOS(cartItemDTOS);
        return shoppingCartDTO;
    }

    @Transactional
    public ShoppingCartDTO updateBySubtractingCartItem(String customerId, Long productId){
        CustomerDocument customer = customerNoSqlRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        ProductDocument product = productNoSqlRepository.findById(String.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        Optional<CartItemDocument> existingCartItem = customer.getShoppingCart().getCartItems().stream()
                .filter(cartItem -> cartItem.getProductDocument().equals(product))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItemDocument cartItem = existingCartItem.get();
            cartItem.setCartItemQuantity(cartItem.getCartItemQuantity() - 1);
            BigDecimal newSubprice = new BigDecimal(product.getProductPrice())
                    .multiply(BigDecimal.valueOf(cartItem.getCartItemQuantity())).setScale(2, RoundingMode.HALF_UP);
            cartItem.setCartItemSubprice(newSubprice);

            if(cartItem.getCartItemQuantity() == 0){
                customer.getShoppingCart().getCartItems().remove(cartItem);
            }
            customerNoSqlRepository.save(customer);

            List<CartItemDTO> cartItemDTOS = CartItemsConvertorNoSql.convertDocumentToDtoList(customer.getShoppingCart().getCartItems());
            ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertorNoSql.convertDocumentToDTO(customer.getShoppingCart());
            BigInteger customerIdBigInt = new BigInteger(customerId, 16);
            shoppingCartDTO.setCustomerId(customerIdBigInt.longValue());
            shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(customer.getShoppingCart()).doubleValue());
            shoppingCartDTO.setCartItemDTOS(cartItemDTOS);

            return shoppingCartDTO;
        }else{
            throw new ProductNotFoundException("Cart item not found");
        }
    }

    @Transactional
    public ShoppingCartDTO deleteCartItem(String customerId , Long productId){
        CustomerDocument customer = customerNoSqlRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        ProductDocument product = productNoSqlRepository.findById(String.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        Optional<CartItemDocument> existingCartItem = customer.getShoppingCart().getCartItems().stream()
                .filter(cartItem -> cartItem.getProductDocument().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItemDocument cartItem = existingCartItem.get();

            customer.getShoppingCart().getCartItems().remove(cartItem);
            customerNoSqlRepository.save(customer);


            List<CartItemDTO> cartItemDTOS = CartItemsConvertorNoSql.convertDocumentToDtoList(customer.getShoppingCart().getCartItems());
            ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertorNoSql.convertDocumentToDTO(customer.getShoppingCart());
            BigInteger customerIdBigInt = new BigInteger(customerId, 16);
            shoppingCartDTO.setCustomerId(customerIdBigInt.longValue());
            shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(customer.getShoppingCart()).doubleValue());
            shoppingCartDTO.setCartItemDTOS(cartItemDTOS);

            return shoppingCartDTO;
        }else{
            throw new ProductNotFoundException("Cart item not found");
        }
    }


    public ShoppingCartDTO getShoppingCartById(String customerId) {
        CustomerDocument customerDocument = customerNoSqlRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        List<CartItemDTO> cartItemDTOList = convertDocumentToDtoList(customerDocument.getShoppingCart().getCartItems());
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setCartItemDTOS(cartItemDTOList);
        shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(customerDocument.getShoppingCart()).doubleValue());
        logger.info("DTO IS BEFORE: " + shoppingCartDTO.toString());
        return shoppingCartDTO;
    }

    @Transactional
    public CartItemDocument updateOrCreateCartItem(ShoppingCartDocument shoppingCart, ProductDocument product, int quantityToAdd) {
        Optional<CartItemDocument> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProductDocument().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItemDocument cartItemToUpdate = existingCartItem.get();
            cartItemToUpdate.setCartItemQuantity(cartItemToUpdate.getCartItemQuantity() + quantityToAdd);
            BigDecimal newSubprice = new BigDecimal(product.getProductPrice())
                    .multiply(BigDecimal.valueOf(cartItemToUpdate.getCartItemQuantity())).setScale(2, RoundingMode.HALF_UP);
            ;
            cartItemToUpdate.setCartItemSubprice(newSubprice);
            return cartItemToUpdate;
        } else {
            logger.info("SHOPPING CART IS BEFORE ::: + " + shoppingCart.toString());
            CartItemDocument cartItemDocument = new CartItemDocument();
            cartItemDocument.setProductDocument(product);
            cartItemDocument.setCartItemQuantity(quantityToAdd);
            cartItemDocument.setCartItemSubprice(new BigDecimal(product.getProductPrice()).multiply(BigDecimal.valueOf(quantityToAdd)).setScale(2, RoundingMode.HALF_UP));

            shoppingCart.getCartItems().add(cartItemDocument);
            logger.info("SHOPPING CART IS AFTER ::: + " + shoppingCart.toString());
            return cartItemDocument;
        }
    }

    private BigDecimal calculateTotalPriceShoppingCart(ShoppingCartDocument shoppingCartDocument) {
        return shoppingCartDocument.getCartItems().stream()
                .map(CartItemDocument::getCartItemSubprice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}

