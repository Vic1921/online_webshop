package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.CustomerUserDetails;
import at.wst.online_webshop.exception_handlers.CustomerNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomerDetailsService implements UserDetailsService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customerOptional  = customerRepository.findByEmail(email);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            // Add your logic to fetch authorities (roles) for the user
            //Collection<? extends GrantedAuthority> authorities = /* fetch authorities */;

            // Create and return CustomUserDetails
            return new CustomerUserDetails(customer, new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User with email does not exist: " + email);
        }
    }

}

