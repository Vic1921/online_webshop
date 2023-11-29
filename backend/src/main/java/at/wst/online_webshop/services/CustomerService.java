package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Customer createCustomer(CreatingCustomerRequest customerRequest) {
        //business logic and validity check
        if(customerRequest.getName().isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if(customerRequest.getAddress().isBlank()){
            throw new IllegalArgumentException("Address cannot be empty");
        }

        if(customerRepository.findByEmail(customerRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("E-mail already used");
        }

        Customer newCustomer = new Customer(customerRequest.getName(), customerRequest.getEmail(), customerRequest.getPassword(), customerRequest.getAddress());


        return customerRepository.save(newCustomer);
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

}
