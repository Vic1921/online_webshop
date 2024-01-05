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
<<<<<<< HEAD
    private String productImageUrl;
    private Integer productTotalSells = 0;
    private VendorDocument vendor; // saves a join with product and vendors, therefore embedding would be a good choice
    //good for the use case of listing and filtering products, but the drawback is the higher redundancy
    //no need for join, more data locality
    //more disk space, and changing the vendor would be expensive
=======
    private String productImage;
    private VendorDocument vendor;
>>>>>>> bf42bcacc87ba3ade0addb0f7c6046c94502bdba
    @DBRef
    private List<ReviewDocument> reviews;
}
