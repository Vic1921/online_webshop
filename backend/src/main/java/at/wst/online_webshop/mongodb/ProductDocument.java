package at.wst.online_webshop.mongodb;

import at.wst.online_webshop.mongodb.embedded.ReviewNoSQL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection="customers")
public class ProductDocument {
    @Id
    private Long productId;

    @Field("productName")
    private String productName;

    @Field("productPrice")
    private Double productPrice;

    @Field("productSKU")
    private String productSKU;

    @Field("productCategory")
    private String productCategory;

    @Field("productDescription")
    private String productDescription;

    @Field("productQuantity")
    private Integer productQuantity;

    @Field("productImageUrl")
    private String productImageUrl;

    @Field("productTotalSells")
    private Integer productTotalSells;

    @DBRef
    @JsonIgnore
    @Field("productReviews")
    private List<ReviewDocument> reviews;

}
