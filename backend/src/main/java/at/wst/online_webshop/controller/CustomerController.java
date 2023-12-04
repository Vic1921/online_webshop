package at.wst.online_webshop.controller;


import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.requests.AuthenticationRequest;
import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import at.wst.online_webshop.exception_handlers.FailedSignUpException;
import at.wst.online_webshop.security.JwtTokenUtil;
import at.wst.online_webshop.services.CustomerService;
import org.apache.coyote.Response;
import org.infinispan.protostream.annotations.impl.HasProtoSchema;
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
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    //SignUp for a customer
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody CreatingCustomerRequest request) {
        try {
            CustomerDTO result = customerService.signUp(request);
            return ResponseEntity.ok(result);
        } catch (FailedSignUpException e) {
            // Handle other exceptions, e.g., unexpected errors
            logger.error("Exception during customer registration", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        //if authorization successful, generate JWT token
        UserDetails userDetails = customerService.loadUserByEmail(request.getEmail());
        System.out.println(userDetails);
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);

        //return jwt token in response
        return new ResponseEntity(result , HttpStatus.OK);
    }
}
