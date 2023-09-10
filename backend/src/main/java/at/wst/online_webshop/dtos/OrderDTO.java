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
public class OrderDTO {

    private Long orderId;
    private String orderDate;
    private Double orderTotalMount;
    private Long customerId;
    private List<Long> productIds;

    public OrderDTO(String orderDate, Double orderTotalMount, Long customerId, List<Long> productIds) {
        this.orderDate = orderDate;
        this.orderTotalMount = orderTotalMount;
        this.customerId = customerId;
        this.productIds = productIds;
    }
}
