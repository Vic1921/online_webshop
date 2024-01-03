package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerConvertorNoSQL {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomerDocument transformCustomer(Customer rdbmsCustomer) {
        try {
            String jsonString = objectMapper.writeValueAsString(rdbmsCustomer);
            CustomerDocument nosqlCustomer = objectMapper.readValue(jsonString, CustomerDocument.class);
            if(rdbmsCustomer.getShoppingCart() == null){
                nosqlCustomer.setShoppingCart(null);
            }
            return nosqlCustomer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

