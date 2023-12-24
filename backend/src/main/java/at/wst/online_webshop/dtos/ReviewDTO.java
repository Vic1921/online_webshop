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
        private Integer reviewRating;
        private String reviewComment;
}
