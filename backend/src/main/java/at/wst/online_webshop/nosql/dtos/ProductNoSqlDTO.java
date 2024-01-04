package at.wst.online_webshop.nosql.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNoSqlDTO {
    private String id;
    private String productName;
    private Double productPrice;
    private String productCategory;
    private String productDescription;
    private Integer productQuantity;
    private String productImage;
    private String vendorName;
}
