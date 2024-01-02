package at.wst.online_webshop.mongodb.embedded;

import at.wst.online_webshop.mongodb.ProductDocument;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

public class OrderItemNoSQL {
    @Id
    private Long orderItemId;

    @Field
    private ProductDocument product;
}
