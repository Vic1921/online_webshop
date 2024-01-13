package at.wst.online_webshop.services;

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
import at.wst.online_webshop.repositories.CartItemRepository;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToDto;
import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToEntity;

@Service
public class ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);
    private final ShoppingCartRepository shoppingCartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Customer customer = customerRepository.findById(shoppingCartDTO.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.GERMAN);
        String formattedCartDate = LocalDateTime.now().format(formatter);
        shoppingCart.setCustomer(customer);
        shoppingCart.setCartDate(formattedCartDate);
        shoppingCartRepository.save(shoppingCart);
        logger.info("After saving shoppingCart: " + shoppingCart.toString());

        return convertToDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDTO getShoppingCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        ShoppingCartDTO shoppingCartDTO = convertToDto(shoppingCart);
        List<CartItemDTO> cartItemDTOList = CartItemConverter.convertToDtoList(shoppingCart.getCartItems());
        shoppingCartDTO.setCartItemDTOS(cartItemDTOList);
        shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(shoppingCart).doubleValue());
        return shoppingCartDTO;
    }

    @Transactional
    public ShoppingCartDTO updateShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        if (shoppingCart == null) {
            logger.info(shoppingCart.toString());
            throw new ShoppingCartNotFoundException("there is no such Shopping cart");
        } else {
            logger.info("NOT NULL" + shoppingCart.toString());
        }
        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(updatedShoppingCart);
    }

    @Transactional
    public void deleteShoppingCart(Long cartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(cartId));

        if (shoppingCart.getCustomer() != null) {
            shoppingCart.getCustomer().setShoppingCart(null);
        }

        shoppingCartRepository.deleteById(cartId);
    }

    @Transactional
    public ShoppingCartDTO addItemToShoppingCart(Long customerId, Long shoppingCartId, Long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));
        logger.info(shoppingCart.toString());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        int quantityToAdd = 1;

        if (product.getProductQuantity() < quantityToAdd) {
            throw new InsufficientProductQuantityException("Not enough quantity available for the product.");
        }


        updateOrCreateCartItem(shoppingCart, product, quantityToAdd);
        //update product and shopping cart
        productRepository.save(product);
        shoppingCartRepository.save(shoppingCart);

        // Convert entities to DTOs
        List<CartItemDTO> cartItemDTOS = CartItemConverter.convertToDtoList(shoppingCart.getCartItems());
        ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertor.convertToDto(shoppingCart);
        shoppingCartDTO.setCustomerId(customerId);
        shoppingCartDTO.setTotalPrice(calculateTotalPriceShoppingCart(shoppingCart).doubleValue());

        shoppingCartDTO.setCartItemDTOS(cartItemDTOS);
        return shoppingCartDTO;
    }

    @Transactional
    public CartItem updateOrCreateCartItem(ShoppingCart shoppingCart, Product product, int quantityToAdd) {
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Update the existing CartItem
            CartItem cartItemToUpdate = existingCartItem.get();
            cartItemToUpdate.setCartItemQuantity(cartItemToUpdate.getCartItemQuantity() + quantityToAdd);
            cartItemToUpdate.setShoppingCart(shoppingCart);

            BigDecimal newSubprice = new BigDecimal(product.getProductPrice())
                    .multiply(BigDecimal.valueOf(cartItemToUpdate.getCartItemQuantity())).setScale(2, RoundingMode.HALF_UP);
            ;
            cartItemToUpdate.setCartItemSubprice(newSubprice);

            // Save the updated CartItem
            cartItemRepository.save(cartItemToUpdate);
            return cartItemToUpdate;
        } else {
            // Create a new CartItem
            CartItem newCartItem = new CartItem(shoppingCart, product, quantityToAdd,
                    new BigDecimal(product.getProductPrice()).multiply(BigDecimal.valueOf(quantityToAdd)).setScale(2, RoundingMode.HALF_UP));
            newCartItem.setShoppingCart(shoppingCart);

            // Add the new CartItem to the shopping cart
            shoppingCart.getCartItems().add(newCartItem);

            // Save the new CartItem
            cartItemRepository.save(newCartItem);
            return newCartItem;
        }
    }

    private BigDecimal calculateTotalPriceShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(CartItem::getCartItemSubprice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}
