package at.wst.online_webshop.dtos;

import at.wst.online_webshop.entities.Product;
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
        private List<ProductDTO> productDTOS;

        public void addProduct(ProductDTO product){
            this.productDTOS.add(product);
        }
}
