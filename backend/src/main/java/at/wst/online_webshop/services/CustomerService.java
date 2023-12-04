package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.exception_handlers.*;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static at.wst.online_webshop.convertors.CustomerConvertor.convertToDto;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
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

        Customer newCustomer = new Customer(customerRequest.getName(), customerRequest.getEmail(), customerRequest.getPassword(), customerRequest.getAddress());

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
        int minLength = 8;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");

        return password.length() >= minLength &&
                hasUppercase &&
                hasLowercase &&
                hasDigit;
    }

    private boolean isPasswordCorrect(String providedPassword, String storedPassword){
    return false;
    }

    public UserDetails loadUserByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if(customer.isPresent()){
            return new org.springframework.security.core.userdetails.User(customer.get().getEmail(), customer.get().getPassword(), new ArrayList<>());
        }else{
            throw new CustomerNotFoundException("User with email does not exist: " + customer.get().getEmail());
        }
    }

}
