package at.wst.online_webshop.nosql.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequestNoSQL {
    private String customerId;
    private String paymentMethod;
    private String shippingDetails;
}
