package at.wst.online_webshop.nosql.documents;

import at.wst.online_webshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orderItems")
public class OrderItemDocument {
    @Id
    private String orderItemId;
    private int quantity;
    private Product product;

}