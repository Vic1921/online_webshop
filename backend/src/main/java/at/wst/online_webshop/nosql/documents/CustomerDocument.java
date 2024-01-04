package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    private String customerId;
    @Indexed(unique = true)
    private String email;
    private AddressDocument address;
    private String name;
    private String password;
    private ShoppingCartDocument shoppingCart;
    // is useful for the Second Report usercase: Customer Review Analysis.
    // could be done wit a query on all reviews, but this is more efficient
    @DBRef
    private List<ReviewDocument> reviews;
    // no point in referencing because this read wont happen so often so embedding would provide sufficient performance
    // TODO: remove DBRef
    @DBRef
    private CustomerDocument recommendedBy;

    @Override
    public String toString() {
        return "CustomerDocument{" +
                "id='" + customerId + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}