package at.wst.online_webshop.controller;


import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.AuthenticationRequest;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.dtos.requests.UpdateShoppingCartRequest;
import at.wst.online_webshop.entities.CustomerUserDetails;
import at.wst.online_webshop.security.JwtTokenUtil;
import at.wst.online_webshop.services.CustomerDetailsService;
import at.wst.online_webshop.services.CustomerService;
import at.wst.online_webshop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/sql/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private ShoppingCartService shoppingCartService;


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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        logger.info("Received login request: " + request.getEmail());
        logger.info("Received login request password: " + request.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        //if authorization successful, generate JWT token
        UserDetails userDetails = customerDetailsService.loadUserByUsername(request.getEmail());

        CustomerUserDetails customerUserDetails = (CustomerUserDetails) userDetails;

        String token = jwtTokenUtil.generateToken(userDetails);
        logger.info("Token generated: " + token);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        if (customerUserDetails.getCustomer().getShoppingCart() != null) {
            result.put("cartID", String.valueOf(customerUserDetails.getCustomer().getShoppingCart().getCartId()));
        } else {
            result.put("cartID", null);
        }
        result.put("customerID", String.valueOf(customerUserDetails.getCustomer().getCustomerId()));
        result.put("customerName", customerUserDetails.getCustomer().getName());
        result.put("customerAddress", customerUserDetails.getCustomer().getAddress().getStreet());

        //return jwt token in response
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-cart")
    public ResponseEntity<String> updateCartId(@RequestBody UpdateShoppingCartRequest request) {
        try {
            customerService.updateShoppingCart(request.getCustomerId(), request.getCartId());
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            logger.info("Getting a bad request " + e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
