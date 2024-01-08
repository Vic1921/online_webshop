package at.wst.online_webshop.nosql.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReviewAnalysis {

    private String customerId;
    private String customerName;
    private List<ReviewDetails> reviews;
    private List<ProductDetails> products;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewDetails {
        private String reviewId;
        private int rating;
        private String comment;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetails {
        private String productId;
        private String productName;
        private Double price;

    }

}
