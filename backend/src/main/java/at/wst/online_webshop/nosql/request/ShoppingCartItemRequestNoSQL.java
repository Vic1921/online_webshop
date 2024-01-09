package at.wst.online_webshop.nosql.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ShoppingCartItemRequestNoSQL {
    private String customerId;
    private Long productId;
}
