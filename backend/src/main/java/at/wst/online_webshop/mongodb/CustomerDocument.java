package at.wst.online_webshop.mongodb;

import at.wst.online_webshop.entities.Address;
import at.wst.online_webshop.entities.Order;
import at.wst.online_webshop.entities.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection="customers")
public class CustomerDocument {

    @Id
    private Long customerId;

    @Field("email")
    private String email;

    @Field("address")
    private Address address;

    @Field("password")
    private String password;

    @Field("name")
    private String name;

    @DBRef
    private ShoppingCart shoppingCart;

    //i think we should reference reviews and embed orders -> we can reference on the n side with the 1:n relationship
    //why reference? because i think that reviews will be updated frequently, a customer will post and delete multiple reviews so referencing would make sense
    //and orders because, the user wants to retrieve his orders frequently so an embedding would make sense
    @DBRef
    private List<ReviewDocument> reviews;

    @Field("orders")
    private List<OrderDocument> orders;

    @DBRef
    private CustomerDocument recommendedBy;
}
