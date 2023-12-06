package at.wst.online_webshop.cutomer;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testSignUp(){
        CreatingCustomerRequest request = new CreatingCustomerRequest();
        request.setName("Max Mustermann");
        request.setEmail("max8989@gmail.com");
        request.setPassword("IMSEistcool23");
        request.setAddress("Währingerstraße 29");

        //Mock repository interactions
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.when(customerRepository.save(customerArgumentCaptor.capture())).thenReturn(new Customer());
        CustomerDTO result = customerService.signUp(request);

        //verify the save method was called with the expected argument
        verify(customerRepository).save(Mockito.any());

        //assertions
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertEquals("Max Mustermann", capturedCustomer.getName());
        assertEquals("max8989@gmail.com", capturedCustomer.getEmail());
        assertEquals("Währingerstraße 29", capturedCustomer.getAddress());
    }

}
