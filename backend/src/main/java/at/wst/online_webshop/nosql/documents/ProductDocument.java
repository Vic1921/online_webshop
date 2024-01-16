package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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
    @Indexed
    private Double productPrice;
    @Indexed(unique = true)
    private String productSKU;
    private String productCategory;
    private String productDescription;
    private Integer productQuantity;
    private String productImageUrl;
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
                ", vendor=" + vendor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDocument that = (ProductDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
