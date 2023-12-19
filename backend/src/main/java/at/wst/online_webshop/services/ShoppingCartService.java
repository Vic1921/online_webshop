package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.CustomerConvertor;
import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exceptions.FailedOrderException;
import at.wst.online_webshop.exceptions.ShoppingCartNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToDto;
import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToEntity;

@Service
public class ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(savedShoppingCart);
    }

    public ShoppingCartDTO getShoppingCartDTOById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        return convertToDto(shoppingCart);
    }

    public ShoppingCart getShoppingCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        return shoppingCart;
    }

    public ShoppingCartDTO updateShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(updatedShoppingCart);
    }

    public void deleteShoppingCart(Long id) {
        shoppingCartRepository.deleteById(id);
    }

    public ShoppingCartDTO addItemToShoppingCart(Long customerId, Long shoppingCartId, Long productId) {
        ShoppingCartDTO shoppingCartDTO = ShoppingCartConvertor.convertToDto(shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId)));
        CustomerConvertor.convertToDto(customerRepository.findById(customerId)
                .orElseThrow(() -> new FailedOrderException("Customer not found.")));
        ProductDTO productDTO = ProductConvertor.convertToDto(productRepository.findById(productId)
                .orElseThrow(() -> new FailedOrderException("Product not found.")));

        //  Subtract product quantity
        productDTO.setProductQuantity(productDTO.getProductQuantity() - 1);

        // Add product to shopping cart
        shoppingCartDTO.addProduct(productDTO);

        // Update product
        shoppingCartDTO = updateShoppingCart(shoppingCartDTO);

        return shoppingCartDTO;
    }
}
