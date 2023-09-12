package at.wst.online_webshop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartItemRequest {
    private Long customerId;
    private Long shoppingCartId;
    private Long productId;
}
