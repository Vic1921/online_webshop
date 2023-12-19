package at.wst.online_webshop.services;

import at.wst.online_webshop.controller.CustomerController;
import at.wst.online_webshop.convertors.CustomerConvertor;
import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.CartItem;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exception_handlers.CustomerNotFoundException;
import at.wst.online_webshop.exception_handlers.InsufficientProductQuantityException;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.exceptions.ShoppingCartNotFoundException;
import at.wst.online_webshop.repositories.CartItemRepository;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToDto;
import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToEntity;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository, ProductRepository productRepository, CartItemRepository cartItemRepository){
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(savedShoppingCart);
    }

    public ShoppingCartDTO getShoppingCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        return convertToDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDTO updateShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        if(shoppingCart == null){
            logger.info(shoppingCart.toString());
            throw new ShoppingCartNotFoundException("there is no such Shopping cart");
        }else{
            logger.info("NOT NULL" + shoppingCart.toString());
        }
        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(updatedShoppingCart);
    }

    @Transactional
    public void deleteShoppingCart(Long id) {
        shoppingCartRepository.deleteById(id);
    }

    @Transactional
    public ShoppingCartDTO addItemToShoppingCart(Long customerId, Long shoppingCartId, Long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new FailedOrderException("Customer not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new FailedOrderException("Product not found."));

        // Subtract product quantity
        int quantityToAdd = 1;

        if (product.getProductQuantity() >= quantityToAdd) {
            product.setProductQuantity(product.getProductQuantity() - quantityToAdd);
        } else {
            throw new InsufficientProductQuantityException("Not enough quantity available for the product.");
        }

        //creating new Cartitem
        updateOrCreateCartItem(shoppingCart, product, quantityToAdd);

        //update product and shopping cart
        productRepository.save(product);
        shoppingCartRepository.save(shoppingCart);

        // Convert entities to DTOs
        ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertor.convertToDto(shoppingCart);
        shoppingCartDTO.setTotalPrice(updatePriceShoppingCartDTO(shoppingCart).doubleValue());
        CustomerDTO customerDTO = CustomerConvertor.convertToDto(customer);
        ProductDTO productDTO = ProductConvertor.convertToDto(product);

        return shoppingCartDTO;
    }

    private void updateOrCreateCartItem(ShoppingCart shoppingCart, Product product, int quantityToAdd){
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Update the existing CartItem
            CartItem cartItemToUpdate = existingCartItem.get();
            cartItemToUpdate.setCartItemQuantity(cartItemToUpdate.getCartItemQuantity() + quantityToAdd);

            BigDecimal newSubprice = new BigDecimal(product.getProductPrice())
                    .multiply(BigDecimal.valueOf(cartItemToUpdate.getCartItemQuantity()));
            cartItemToUpdate.setCartItemSubprice(newSubprice);

            // Save the updated CartItem
            cartItemRepository.save(cartItemToUpdate);
        } else {
            // Create a new CartItem
            CartItem newCartItem = new CartItem(shoppingCart, product, quantityToAdd,
                    new BigDecimal(product.getProductPrice()).multiply(BigDecimal.valueOf(quantityToAdd)));

            // Add the new CartItem to the shopping cart
            shoppingCart.getCartItems().add(newCartItem);

            // Save the new CartItem
            cartItemRepository.save(newCartItem);
        }
    }

    private BigDecimal updatePriceShoppingCartDTO(ShoppingCart shoppingCart){
        return shoppingCart.getCartItems().stream()
                .map(CartItem::getCartItemSubprice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
