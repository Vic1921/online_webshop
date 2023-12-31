package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    private String address;
    private String name;
    // Other fields like password should be handled with care for security

    @DBRef
    private List<OrderDocument> orders;

    @DBRef
    private List<ReviewDocument> reviews;

}