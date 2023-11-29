package at.wst.online_webshop.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatingCustomerRequest {
    private String name;
    private String email;
    private String password;
    private String address;
}
