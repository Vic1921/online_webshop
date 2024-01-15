package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDocument {
    private List<CartItemDocument> cartItems;
    private LocalDateTime cartDate;

    @Override
    public String toString() {
        return "ShoppingCartDocument{" +
                "cartItems=" + cartItems +
                '}';
    }
}
