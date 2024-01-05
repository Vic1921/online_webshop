package at.wst.online_webshop.nosql.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class ReviewDocument {
    @Id
    private String id;
    private Integer reviewRating;
    private String reviewComment;
    @Indexed
    private LocalDateTime reviewDate;

    @DBRef
    private ProductDocument product;

    @DBRef
    private CustomerDocument customer;

}
