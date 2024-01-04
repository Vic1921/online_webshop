package at.wst.online_webshop.nosql.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ShoppingCartNoSqlDTO {
    private List<CartItemNoSqlDTO> cartItems;
    private double totalPrice;

}
