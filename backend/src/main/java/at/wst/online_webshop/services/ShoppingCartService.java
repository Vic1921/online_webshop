package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.CustomerConvertor;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exception_handlers.FailedOrderException;
import at.wst.online_webshop.exception_handlers.ShoppingCartNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import at.wst.online_webshop.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToDto;
import static at.wst.online_webshop.convertors.ShoppingCartConvertor.convertToEntity;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDTO);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return convertToDto(savedShoppingCart);
    }

    public ShoppingCartDTO getShoppingCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        return convertToDto(shoppingCart);
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

        shoppingCartDTO.addProduct(productId);

        shoppingCartDTO = updateShoppingCart(shoppingCartDTO);
        shoppingCartRepository.save(ShoppingCartConvertor.convertToEntity(shoppingCartDTO));

        // TODO - Subtract product quantity

        return shoppingCartDTO;
    }
}
