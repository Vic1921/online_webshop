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
    private ProductDTO productDTO;
    private int orderItemQuantity;


    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "orderItemId=" + orderItemId +
                ", productDTO=" + productDTO +
                ", orderItemQuantity=" + orderItemQuantity +
                '}';
    }
}
