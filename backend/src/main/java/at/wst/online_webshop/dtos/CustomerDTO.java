package at.wst.online_webshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {

    private Long customerId;
    private String email;
    private String address;
    private String name;
    private Long shoppingCartId;
    private Long recommendedByCustomerId;
    private List<Long> orderIds;
    private List<Long> reviewIds;

}
