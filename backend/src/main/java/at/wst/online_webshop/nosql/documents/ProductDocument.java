package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class ProductDocument {
    @Id
    private String id;
    @Indexed
    private String productName;
    private Double productPrice;
    @Indexed(unique = true)
    private String productSKU;
    private String productCategory;
    private String productDescription;
    private Integer productQuantity;
    private String productImage;
    private VendorDocument vendor;
    @DBRef
    private List<ReviewDocument> reviews;
}
