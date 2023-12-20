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
    private List<OrderItemDTO> orderItems;

    public OrderDTO(String orderDate, Double orderTotalMount, Long customerId, List<OrderItemDTO> orderItems) {
        this.orderDate = orderDate;
        this.orderTotalMount = orderTotalMount;
        this.customerId = customerId;
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", orderTotalMount=" + orderTotalMount +
                ", customerId=" + customerId +
                ", orderItems=" + orderItems +
                '}';
    }
}
