package at.wst.online_webshop.nosql.controller;


import at.wst.online_webshop.dtos.ReviewDTO;
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
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewNoSQLService.getReviewsByProductId(productId);
        logger.info(reviews.toString());
        return ResponseEntity.ok(reviews);
    }


}
