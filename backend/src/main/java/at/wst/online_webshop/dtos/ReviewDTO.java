package at.wst.online_webshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDTO {

    private Long reviewId;
    private Long productId;
    private String customerName;
    private String reviewDate;
    private Integer reviewRating;
    private String reviewComment;
    private Long customerId;

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", customerName='" + customerName + '\'' +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewRating=" + reviewRating +
                ", reviewComment='" + reviewComment + '\'' +
                '}';
    }
}
