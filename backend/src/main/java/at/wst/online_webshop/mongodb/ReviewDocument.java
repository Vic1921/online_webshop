package at.wst.online_webshop.mongodb;

import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.Product;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "reviews")
public class ReviewDocument {
    @Id
    private Long reviewId;

    @DBRef
    @Field("product")
    private ProductDocument product;

    @Field("reviewer")
    private Customer customer;

    @Field("reviewRating")
    private Integer reviewRating;

    @Field("comment")
    private String reviewComment;

    @Field("reviewDate")
    private String reviewDate;

    public ReviewDocument(ProductDocument product, Customer customer, int rating, String comment, String reviewDate) {
        this.product = product;
        this.customer = customer;
        this.reviewRating = rating;
        this.reviewComment = comment;
        this.reviewDate = reviewDate;
    }






}
