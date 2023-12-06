package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.exception_handlers.CustomerNotFoundException;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if (customer.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    customer.get().getEmail(),
                    customer.get().getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User with email does not exist: " + email);
        }
    }

}

