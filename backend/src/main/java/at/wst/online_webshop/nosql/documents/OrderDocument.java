package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class OrderDocument {
    @Id
    private String id;
    @Indexed
    private LocalDateTime orderDate;
    private Double orderTotalMount;

    @DBRef
    private CustomerDocument customer;

    // embedding the orderItems would make sense, because first 1:n relationship,
    // and second uppn fetching the details of the order, you can immediately see what the order items are in the order
    private List<OrderItemDocument> orderItems;

}