package at.wst.online_webshop.nosql.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemNoSqlDTO {
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Double productPrice;
    private String productCategory;
    private int cartItemQuantity;
    private Double cartItemSubprice;
}
