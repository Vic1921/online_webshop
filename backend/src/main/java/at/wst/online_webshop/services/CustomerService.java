package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.OrderConvertor;
import at.wst.online_webshop.convertors.ShoppingCartConvertor;
import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.dtos.ShoppingCartDTO;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.ShoppingCart;
import at.wst.online_webshop.exception_handlers.*;
import at.wst.online_webshop.exceptions.ShoppingCartNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static at.wst.online_webshop.convertors.CustomerConvertor.convertToDto;
import static at.wst.online_webshop.convertors.OrderConvertor.convertToEntity;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final ShoppingCartService shoppingCartService;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, ShoppingCartService shoppingCartService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartService = shoppingCartService;
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomer(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    public ShoppingCartDTO updateShoppingCart(Long customerId, Long cartId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            // Assuming you have a method to fetch the shopping cart by cartId
           ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCartById(cartId);

            if (shoppingCartDTO != null) {
                // Update the customer entity with the new shopping cart
                ShoppingCart shoppingCart = ShoppingCartConvertor.convertToEntity(shoppingCartDTO);
                customer.setShoppingCart(shoppingCart);

                // Save the updated customer
                customerRepository.save(customer);

                // Convert the updated shopping cart to DTO
                return shoppingCartDTO;
            } else {
                // Handle the case when the shopping cart is not found
                throw new ShoppingCartNotFoundException("Shopping cart not found");
            }
        } else {
            // Handle the case when the customer is not found
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Transactional
    public CustomerDTO signUp(CreatingCustomerRequest customerRequest) {
        //business logic and validity check
        if (customerRequest.getName().isBlank()) {
            throw new FailedSignUpException("Name cannot be empty");
        }

        if (customerRequest.getAddress().isBlank()) {
            throw new FailedSignUpException("Address cannot be empty");
        }

        if (customerRepository.findByEmail(customerRequest.getEmail()).isPresent()) {
            throw new ExistingEmailException("E-mail already used");
        }

        if (!isValidEmail(customerRequest.getEmail())) {
            throw new InvalidEmailException("E-mail is not valid");
        }

        if (!isSecurePassword(customerRequest.getPassword())) {
            throw new WeakPasswordException("Password is not secure");
        }

        String encodedPassword = passwordEncoder.encode(customerRequest.getPassword());

        Customer newCustomer = new Customer(customerRequest.getName(), customerRequest.getEmail(), encodedPassword, customerRequest.getAddress());

        customerRepository.save(newCustomer);
        return convertToDto(newCustomer);
    }


    private boolean isValidEmail(String email){
        //using regular expression to check if email is valid
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isSecurePassword(String password){
        //password criteria definition
        /*
        int minLength = 8;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");

        return password.length() >= minLength &&
                hasUppercase &&
                hasLowercase &&
                hasDigit;

         */
        return true;
    }
}
