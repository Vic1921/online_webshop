package at.wst.online_webshop.nosql.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartItemRequestNoSQL {
    private Long customerId;
    private Long productId;
}
