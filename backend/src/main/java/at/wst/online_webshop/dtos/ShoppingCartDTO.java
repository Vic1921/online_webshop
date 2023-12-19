package at.wst.online_webshop.dtos;

import at.wst.online_webshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartDTO {

        private Long cartId;
        private Double totalPrice;
        private Long customerId;
        private List<CartItemDTO> cartItemDTOS;

        public void addProduct(CartItemDTO cartItemDTO){
                if(cartItemDTOS == null){
                        cartItemDTOS = new ArrayList<CartItemDTO>();
                }
                this.cartItemDTOS.add(cartItemDTO);
        }
}
