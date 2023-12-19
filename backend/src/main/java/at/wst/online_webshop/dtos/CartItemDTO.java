package at.wst.online_webshop.dtos;

import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.entities.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private int cartItemQuantity;
    private BigDecimal cartItemSubprice;
}
