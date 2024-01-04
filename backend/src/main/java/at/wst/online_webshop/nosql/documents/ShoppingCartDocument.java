package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//we embed shopping cart to customer
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDocument{
    private List<CartItemDocument> cartItems;


}
