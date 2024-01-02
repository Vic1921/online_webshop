package at.wst.online_webshop.mongodb;

import at.wst.online_webshop.mongodb.embedded.OrderItemNoSQL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDocument {
    @Id
    private Long orderId;

    @Field("orderDate")
    private String orderDate;

    @Field("orderTotalMount")
    private Double orderTotalMount;

    //embedding would make sense, because its a 1:1 relationship
    @Field
    private CustomerDocument customer;

    // embedding the orderItems would make sense, because first 1:n relationship,
    // second we query often through the order_items in my report,
    // and uppn fetching the details of the order, you can see immediately what the order item are in the order
    @Field("orderItems")
    private List<OrderItemNoSQL> orderItems;

    public OrderDocument(String orderDate, Double orderTotalMount, List<OrderItemNoSQL> orderItems) {
        this.orderDate = orderDate;
        this.orderTotalMount = orderTotalMount;
        this.orderItems = orderItems;
    }

}
