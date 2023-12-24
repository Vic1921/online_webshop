package at.wst.online_webshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "review_rating", nullable = false)
    private Integer reviewRating;

    @Column(name = "review_comment", nullable = false)
    private String reviewComment;

    public Review(Product product, Customer customer, int rating, String comment) {
        this.product = product;
        this.customer = customer;
        this.reviewRating = rating;
        this.reviewComment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", product=" + product +
                ", customer=" + customer +
                ", reviewRating=" + reviewRating +
                ", reviewComment='" + reviewComment + '\'' +
                '}';
    }
}
