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
    private String productImageUrl;
    private Double productPrice;
    private String productCategory;
    private int cartItemQuantity;
    private long shoppingCartId;
    private BigDecimal cartItemSubprice;

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productPrice=" + productPrice +
                ", cartItemQuantity=" + cartItemQuantity +
                ", cartItemSubprice=" + cartItemSubprice +
                '}';
    }
}
