package at.wst.online_webshop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateReviewRequest {
    private Long customerId;
    private Long productId;
    private String comment;
    private int rating;
    private Long orderId;
}
