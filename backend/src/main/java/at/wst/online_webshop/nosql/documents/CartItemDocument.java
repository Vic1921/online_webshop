package at.wst.online_webshop.nosql.documents;

import at.wst.online_webshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDocument {
    @Id
    private String cartItemId;
    //Because Product is referenced by multiple entities, by the CartItem and by the OrderItem
    @DBRef
    private ProductDocument productDocument;
    private Integer cartItemQuantity;
    private BigDecimal cartItemSubprice;

    public CartItemDocument(ProductDocument productDocument, int cartItemQuantity, BigDecimal cartItemSubprice) {
        this.productDocument = productDocument;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemSubprice = cartItemSubprice;

    }


}
