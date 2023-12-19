package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.entities.Customer;
import org.modelmapper.ModelMapper;

public class CustomerConvertor {
    // TODO: Add an Interface Convertor<T, S> to implement all the convertors
    private static ModelMapper modelMapper = new ModelMapper();

    public static CustomerDTO convertToDto(Customer customer) {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    public static Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        return customer;
    }
}
