package at.wst.online_webshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartDTO {

        private Long cartId;
        private Double totalPrice;
        private Long customerId;
        private List<Long> productIds;

        public void addProduct(Long productId){
            this.productIds.add(productId);
        }
}
