package at.wst.online_webshop;

import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
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
    public void saveCustomerTest(){
        //arrange
        Customer customer = new Customer("Max Mustermann", "max8989@gmail.com", "password", "Währingerstraße 29");

        //act
        Customer savedCustomer = customerRepository.save(customer);

        //assert
        assertThat(savedCustomer.getCustomerId() != null);
        assertEquals(savedCustomer.getName(), customer.getName());
        assertEquals(savedCustomer.getCustomerId(), customer.getCustomerId());
        assertEquals(savedCustomer.getAddress(), customer.getAddress());
        assertEquals(savedCustomer.getOrders(), customer.getOrders());
        assertEquals(savedCustomer.getReviews(), customer.getReviews());
        assertEquals(savedCustomer.getShoppingCart(), customer.getShoppingCart());
    }
}
