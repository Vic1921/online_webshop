package at.wst.online_webshop.nosql.documents;

import at.wst.online_webshop.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class CustomerDocument {
    @Id
    private String id;
    private String email;
    private AddressDocument address;
    private String name;
    // Other fields like password should be handled with care for security
    @Field("encryptedPassword")
    private String password;
    private ShoppingCartDocument shoppingCart;
    @DBRef
    private List<OrderDocument> orders;

    @DBRef
    private List<ReviewDocument> reviews;

    private Customer recommendedBy;

}