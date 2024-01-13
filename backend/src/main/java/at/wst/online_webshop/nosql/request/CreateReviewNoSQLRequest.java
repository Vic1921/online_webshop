package at.wst.online_webshop.nosql.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateReviewNoSQLRequest {
    private String customerId;
    private String productId;
    private String comment;
    private int rating;

}
