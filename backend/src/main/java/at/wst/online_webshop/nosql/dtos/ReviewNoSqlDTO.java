package at.wst.online_webshop.nosql.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewNoSqlDTO {
    private String reviewId;
    private Integer reviewRating;
    private String reviewComment;
    private String reviewDate;
    private String productId;
    private String customerName;
}
