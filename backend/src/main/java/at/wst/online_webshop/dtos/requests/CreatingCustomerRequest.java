package at.wst.online_webshop.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatingCustomerRequest {
    private String name;
    private String email;
    private String password;
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
