package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

public class CustomerConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static CustomerDTO convertJsonToDTO(CustomerDocument customerDocument) {
        try {
            CustomerDTO customerDTO =  modelMapper.map(customerDocument, CustomerDTO.class);

            return customerDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting JSON to CustomerDTO", e);
        }
    }
}

