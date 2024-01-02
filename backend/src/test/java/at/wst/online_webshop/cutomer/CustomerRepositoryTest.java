package at.wst.online_webshop.cutomer;

import at.wst.online_webshop.entities.Address;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {
        // Arrange
        Address address = new Address("Währingerstraße 29", "Vienna", "1010", "Austria");
        Customer customer = new Customer("Max Mustermann", "max8989@gmail.com", "password", address);

        // Act
        Customer savedCustomer = customerRepository.save(customer);

        // Assert
        assertThat(savedCustomer.getCustomerId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(savedCustomer.getCustomerId()).isEqualTo(customer.getCustomerId());

        // Check the address details
        assertThat(savedCustomer.getAddress()).isNotNull();
        assertThat(savedCustomer.getAddress().getStreet()).isEqualTo(address.getStreet());
        assertThat(savedCustomer.getAddress().getCity()).isEqualTo(address.getCity());
        assertThat(savedCustomer.getAddress().getPostalCode()).isEqualTo(address.getPostalCode());
        assertThat(savedCustomer.getAddress().getCountry()).isEqualTo(address.getCountry());

        assertThat(savedCustomer.getOrders()).isEmpty();
        assertThat(savedCustomer.getReviews()).isEmpty();
        assertThat(savedCustomer.getShoppingCart()).isNull();
    }
}
