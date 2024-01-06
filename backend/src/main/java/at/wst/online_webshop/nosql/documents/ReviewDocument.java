package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class ReviewDocument {
    @Id
    private String reviewId;
    private Integer reviewRating;
    private String reviewComment;
    @Indexed
    private LocalDate reviewDate;

    @DBRef
    private ProductDocument product;

    //i delete this because bi directional referencing in nosql impacts extremely the performance, i noticed it in the migration process where it takes forever,
    //i think we should generally avoid circular dependencies in nosql,
    /*
    @DBRef
    private CustomerDocument customer;
*/
    @Override
    public String toString() {
        return "ReviewDocument{" +
                "id='" + reviewId + '\'' +
                ", reviewRating=" + reviewRating +
                ", reviewComment='" + reviewComment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
