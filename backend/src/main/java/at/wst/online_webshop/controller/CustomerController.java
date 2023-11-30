package at.wst.online_webshop.controller;


import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    //SignUp for a customer
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody CreatingCustomerRequest request) {
        try {
            CustomerDTO result = customerService.signUp(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Handle other exceptions, e.g., unexpected errors
            logger.error("Exception during customer registration", e);
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}
