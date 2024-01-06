package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String productImageUrl;
    private Integer productTotalSells = 0;
    private VendorDocument vendor;

    @Override
    public String toString() {
        return "ProductDocument{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productSKU='" + productSKU + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productQuantity=" + productQuantity +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productTotalSells=" + productTotalSells +
                ", vendor=" + vendor +
                '}';
    }
}
