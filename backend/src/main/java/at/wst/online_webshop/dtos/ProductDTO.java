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
    private String productImageUrl;
    private String productDescription;
    private String productCategory;
    private Integer productQuantity;
    private List<Long> reviewIds;
}
