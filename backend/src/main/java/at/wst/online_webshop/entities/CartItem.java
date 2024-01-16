package at.wst.online_webshop.entities;

import at.wst.online_webshop.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @Column(name = "cart_item_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int cartItemQuantity;

    @Column(nullable = false)
    private BigDecimal cartItemSubprice;

    public CartItem(ShoppingCart shoppingCart, Product product, int cartItemQuantity, BigDecimal cartItemSubprice) {
        this.shoppingCart = shoppingCart;
        this.product = product;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemSubprice = cartItemSubprice;

    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", product=" + product +
                ", cartItemQuantity=" + cartItemQuantity +
                ", cartItemSubprice=" + cartItemSubprice +
                '}';
    }
}
