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
    @Indexed
    private Integer reviewRating;
    private String reviewComment;
    private LocalDate reviewDate;

    @DBRef
    private ProductDocument product;

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
