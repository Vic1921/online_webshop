package at.wst.online_webshop.nosql.controller;


import at.wst.online_webshop.nosql.dtos.ReviewNoSqlDTO;
import at.wst.online_webshop.nosql.request.CreateReviewNoSQLRequest;
import at.wst.online_webshop.nosql.services.ReviewNoSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewNoSQLController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewNoSQLController.class);
    private final ReviewNoSQLService reviewNoSQLService;

    @Autowired
    public ReviewNoSQLController(ReviewNoSQLService reviewNoSQLService) {
        this.reviewNoSQLService = reviewNoSQLService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewNoSqlDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewNoSqlDTO> reviews = reviewNoSQLService.getReviewsWithCustomerByProductId(String.valueOf(productId));
        logger.info(reviews.toString());
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ReviewNoSqlDTO> addReview(@RequestBody CreateReviewNoSQLRequest request) {
        logger.info("WRITING REVIEW -> " + request.toString());

        try {
            ReviewNoSqlDTO result = reviewNoSQLService.addReview(
                    request.getCustomerId(),
                    request.getProductId(),
                    request.getComment(),
                    request.getRating());

            logger.info("Review added successfully");
            return ResponseEntity.ok(result);
        } catch (Exception e){
            logger.warn("Failed to add review");
            return ResponseEntity.badRequest().body(null);
        }

    }


}
