package at.wst.online_webshop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceOrderRequest {
    private Long customerId;
    private Long shoppingCartId;
    private String paymentMethod;
    private String shippingDetails;

    @Override
    public String toString() {
        return "PlaceOrderRequest{" +
                "customerId=" + customerId +
                ", shoppingCartId=" + shoppingCartId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", shippingDetails='" + shippingDetails + '\'' +
                '}';
    }
}
