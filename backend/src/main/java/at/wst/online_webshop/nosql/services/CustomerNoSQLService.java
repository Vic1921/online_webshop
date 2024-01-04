package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.exceptions.*;
import at.wst.online_webshop.nosql.documents.AddressDocument;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

import static at.wst.online_webshop.nosql.convertors.CustomerConvertorNoSQL.convertJsonToDTO;

@Service
public class CustomerNoSQLService {
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerNoSQLService(CustomerNoSqlRepository customerNoSqlRepository, PasswordEncoder passwordEncoder) {
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CustomerDTO signUp(CreatingCustomerRequest customerRequest) {
        if (customerRequest.getName().isBlank()) {
            throw new FailedSignUpException("Name cannot be empty");
        }

        if (customerRequest.getStreet().isBlank()) {
            throw new FailedSignUpException("Street cannot be empty");
        }

        if (customerRequest.getCountry().isBlank()) {
            throw new FailedSignUpException("Country cannot be empty");
        }

        if (customerRequest.getPostalCode().isBlank()) {
            throw new FailedSignUpException("Postal code cannot be empty");
        }

        if (customerRequest.getCity().isBlank()) {
            throw new FailedSignUpException("City cannot be empty");
        }

        if (customerNoSqlRepository.findByEmail(customerRequest.getEmail()).isPresent()) {
            throw new ExistingEmailException("E-mail already used");
        }

        if (!isValidEmail(customerRequest.getEmail())) {
            throw new InvalidEmailException("E-mail is not valid");
        }

        if (!isSecurePassword(customerRequest.getPassword())) {
            throw new WeakPasswordException("Password is not secure");
        }

        String encodedPassword = passwordEncoder.encode(customerRequest.getPassword());
        String street = customerRequest.getStreet();
        String city = customerRequest.getCity();
        String postalCode = customerRequest.getPostalCode();
        String country = customerRequest.getCountry();
        AddressDocument address = new AddressDocument(street, city, postalCode, country);
        CustomerDocument newCustomer = new CustomerDocument();
        newCustomer.setName(customerRequest.getName());
        newCustomer.setEmail(customerRequest.getEmail());
        newCustomer.setPassword(encodedPassword);
        newCustomer.setAddress(address);

        customerNoSqlRepository.save(newCustomer);

        return convertJsonToDTO(newCustomer);
    }

    private boolean isValidEmail(String email){
        //using regular expression to check if email is valid
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public CustomerDocument getCustomerByEmail(String email) {
        Optional<CustomerDocument> optionalCustomer = customerNoSqlRepository.findByEmail(email);
        return optionalCustomer.orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
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
