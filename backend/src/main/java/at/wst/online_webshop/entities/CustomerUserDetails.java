package at.wst.online_webshop.entities;

import at.wst.online_webshop.entities.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomerUserDetails extends User {

    private final Customer customer;

    public CustomerUserDetails(Customer customer, Collection<? extends GrantedAuthority> authorities) {
        super(customer.getEmail(), customer.getPassword(), authorities);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
