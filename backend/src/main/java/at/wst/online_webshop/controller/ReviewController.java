package at.wst.online_webshop.controller;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.dtos.requests.CreateReviewRequest;
import at.wst.online_webshop.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {
    private final ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);


    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    // Add a review
    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@RequestBody CreateReviewRequest request) {
        ReviewDTO result = reviewService.addReview(
                request.getCustomerId(),
                request.getProductId(),
                request.getComment(),
                request.getRating(),
                request.getOrderId());

        return ResponseEntity.ok(result);
    }

    // Get reviews for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        logger.info(reviews.toString());
        return ResponseEntity.ok(reviews);
    }
}
