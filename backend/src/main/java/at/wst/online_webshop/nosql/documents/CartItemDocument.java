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
    @DBRef
    private ProductDocument productDocument;
    private Integer cartItemQuantity;
    private BigDecimal cartItemSubprice;

}
