package at.wst.online_webshop.nosql.documents;

import at.wst.online_webshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDocument {
    @Id
    private Long cartItemId;
    private Product product;
    private Integer cartItemQuantity;
    private BigDecimal cartItemSubprice;

    public CartItemDocument(Product product, int cartItemQuantity, BigDecimal cartItemSubprice) {
        this.product = product;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemSubprice = cartItemSubprice;

    }


}
