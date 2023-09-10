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
public class ProductDTO {

    private Long productId;
    private String productName;
    private Double productPrice;
    private String productSKU;
    private Integer productQuantity;
    private Long vendorId;
    private Long shoppingCartId;
    private List<Long> reviewIds;
    private List<Long> orderIds;
}
