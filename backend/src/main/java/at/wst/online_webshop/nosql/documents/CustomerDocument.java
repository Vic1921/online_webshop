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
    @DBRef
    private List<ReviewDocument> reviews;
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