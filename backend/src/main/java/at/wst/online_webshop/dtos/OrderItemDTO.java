package at.wst.online_webshop.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO {
    private Long orderItemId;
    private Long productId;
    private int orderItemQuantity;

}
