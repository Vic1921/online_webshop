package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//we embed shopping cart to customer
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDocument{
    private String cartId;
    //we want to embed, because we want to perform atomic updates on the embedded document individually -> better consistency,
    //update specific fields like quantity and sub total price,
    //we have to update the whole ShoppingCart,so frequent updates would lead to complex operations
    //but the documt size is fairly small i think, because a shopping cart wouldnt be that large
    //and we want to display the content of the shoppingcart often, so an embedding would make sense
    List<CartItemDocument> cartItems;

    public ShoppingCartDocument(List<CartItemDocument> cartItems) {
        this.cartItems = cartItems;
    }

}
