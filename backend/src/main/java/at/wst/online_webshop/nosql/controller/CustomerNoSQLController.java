package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.AuthenticationRequest;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import at.wst.online_webshop.nosql.services.CustomerNoSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nosql/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerNoSQLController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerNoSQLController.class);
    @Autowired
    private CustomerNoSQLService customerNoSQLService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerNoSqlRepository customerNoSqlRepository;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody CreatingCustomerRequest request) {
        try {
            CustomerDTO result = customerNoSQLService.signUp(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Exception during customer registration", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        logger.info("Received login request: " + request.getEmail());
        logger.info("Received login request password: " + request.getPassword());
        String enteredPassword = request.getPassword();
        CustomerDocument customerDocument = customerNoSQLService.getCustomerByEmail(request.getEmail());

        if (passwordEncoder.matches(enteredPassword, customerDocument.getPassword())) {
            Map<String, String> result = new HashMap<>();
            result.put("customerID", String.valueOf(customerDocument.getCustomerId()));
            result.put("customerName", customerDocument.getName());
            result.put("customerAddress", customerDocument.getAddress().getStreet());
            if (customerDocument.getShoppingCart() == null) {
                result.put("cartID", null);
            } else {
                result.put("cartID", "1");
            }
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

}